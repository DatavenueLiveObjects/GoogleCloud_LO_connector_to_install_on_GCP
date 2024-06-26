/**
 * Copyright (c) Orange, Inc. and its affiliates. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;


import com.google.common.collect.Lists;
import com.orange.lo.sample.lo2pubsub.pubsub.PubSubMessageSender;
import com.orange.lo.sample.lo2pubsub.utils.ConnectorHealthActuatorEndpoint;
import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.fifomqtt.DataManagementFifo;
import com.orange.lo.sdk.mqtt.exceptions.LoMqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@EnableScheduling
@Component
public class LoMqttSynchronizationService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PubSubMessageSender pubSubMessageSender;
    private final Queue<LoMessage> messageQueue;
    private final DataManagementFifo dataManagementFifo;
    private final LoProperties loProperties;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint;

    private static final int DEFAULT_BATCH_SIZE = 10;

    public LoMqttSynchronizationService(
            LOApiClient loApiClient,
            PubSubMessageSender pubSubMessageSender,
            Queue<LoMessage> messageQueue,
            LoProperties loProperties,
            ThreadPoolExecutor threadPoolExecutor,
            ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint
    ) {
        LOG.info("LoMqttSynchronizationService init...");

        this.pubSubMessageSender = pubSubMessageSender;
        this.messageQueue = messageQueue;
        this.dataManagementFifo = loApiClient.getDataManagementFifo();
        this.loProperties = loProperties;
        this.threadPoolExecutor = threadPoolExecutor;
        this.connectorHealthActuatorEndpoint = connectorHealthActuatorEndpoint;
    }

    @PostConstruct
    public void start() {
        LOG.info("Start receiving messages...");

        try {
            dataManagementFifo.connect();
        } catch (LoMqttException e) {
            LOG.error("Problem with connection. Check LO credentials. ", e);
            connectorHealthActuatorEndpoint.setLoConnectionStatus(false);
        }
    }

    @PreDestroy
    public void stop() {
        LOG.info("Stop receiving messages...");
        dataManagementFifo.disconnect();
    }

    @Scheduled(fixedRateString = "${lo.synchronization-interval}")
    public void synchronize() {
        if (!dataManagementFifo.isConnected() && connectorHealthActuatorEndpoint.isCloudConnectionStatus() && connectorHealthActuatorEndpoint.isLoConnectionStatus())
            dataManagementFifo.connectAndSubscribe();

        if (!messageQueue.isEmpty()) {
            int batchSize = loProperties.getMessageBatchSize() != null ? loProperties.getMessageBatchSize() : DEFAULT_BATCH_SIZE;
            List<List<LoMessage>> messagePackages = Lists.partition(new LinkedList<>(messageQueue), batchSize);

            List<SynchronizationTask> tasks = messagePackages
                    .stream()
                    .map(messageList -> new SynchronizationTask(pubSubMessageSender, messageList))
                    .collect(Collectors.toList());

            try {
                threadPoolExecutor.invokeAll(tasks);
                messageQueue.clear();
            } catch (InterruptedException e) {
                LOG.error("Error in message synchronization process", e);
            }
        }
    }
}