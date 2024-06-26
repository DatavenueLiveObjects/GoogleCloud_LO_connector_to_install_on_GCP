/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.orange.lo.sample.lo2pubsub.liveobjects.LoMessage;
import com.orange.lo.sample.lo2pubsub.liveobjects.LoProperties;
import com.orange.lo.sample.lo2pubsub.utils.ConnectorHealthActuatorEndpoint;
import com.orange.lo.sample.lo2pubsub.utils.Counters;
import com.orange.lo.sdk.LOApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class PubSubMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Publisher publisher;
    private final Counters counters;
    private final ApiFuturesCallbackSupport apiFuturesCallbackSupport;
    private final LOApiClient loApiClient;
    private final LoProperties loProperties;
    private final ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint;
    private final CheckConnectionApiFutureCallbackImpl checkConnectionApiFutureCallback;


    public PubSubMessageSender(
            Publisher publisher,
            Counters counters,
            ApiFuturesCallbackSupport apiFuturesCallbackSupport,
            LOApiClient loApiClient,
            LoProperties loProperties,
            ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint,
            CheckConnectionApiFutureCallbackImpl apiFutureCallback
    ) {
        this.publisher = publisher;
        this.counters = counters;
        this.apiFuturesCallbackSupport = apiFuturesCallbackSupport;
        this.loApiClient = loApiClient;
        this.loProperties = loProperties;
        this.connectorHealthActuatorEndpoint = connectorHealthActuatorEndpoint;
        this.checkConnectionApiFutureCallback = apiFutureCallback;
    }

    public void sendMessages(List<LoMessage> messageList) {
        messageList.forEach(this::sendMessage);
    }

    private void sendMessage(LoMessage message) {
        counters.getMesasageSentAttemptCounter().increment();

        ByteString data = ByteString.copyFromUtf8(message.getMessage());
        PubsubMessage pubsubMessage = PubsubMessage
                .newBuilder()
                .setData(data)
                .build();
        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

        apiFuturesCallbackSupport.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Unable to publish message ", throwable);
                counters.getMesasageSentFailedCounter().increment();
                loApiClient.getDataManagementFifo().sendAck(message.getMessageId(), loProperties.getMessageQos());
                connectorHealthActuatorEndpoint.setCloudConnectionStatus(false);
                LOGGER.error("Problem with connection. Check GCP credentials. ", throwable);
            }

            @Override
            public void onSuccess(String messageId) {
                LOGGER.debug("Message {} published", messageId);
                counters.getMesasageSentCounter().increment();
                loApiClient.getDataManagementFifo().sendAck(message.getMessageId(), loProperties.getMessageQos());
                connectorHealthActuatorEndpoint.setCloudConnectionStatus(true);
            }
        });

    }

    @PostConstruct
    private void checkConnection() {
        ByteString data = ByteString.copyFromUtf8(" ");
        PubsubMessage pubsubMessage = PubsubMessage
                .newBuilder()
                .setData(data)
                .build();
        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

        apiFuturesCallbackSupport.addCallback(messageIdFuture, checkConnectionApiFutureCallback);
    }
}