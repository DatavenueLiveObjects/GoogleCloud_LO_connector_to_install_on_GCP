/**
 * Copyright (c) Orange, Inc. and its affiliates. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.orange.lo.sample.lo2pubsub.utils.CountersProvider;
import com.orange.lo.sdk.fifomqtt.DataManagementFifoCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Queue;

@Component
public class LoMqttMessageHandler implements DataManagementFifoCallback {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CountersProvider countersProvider;
    private final Queue<String> messageQueue;

    public LoMqttMessageHandler(CountersProvider countersProvider, Queue<String> messageQueue) {
        LOG.info("LoMqttHandler init...");

        this.countersProvider = countersProvider;
        this.messageQueue = messageQueue;
    }

    @Override
    public void onMessage(String message) {
        countersProvider.evtReceived().increment();
        messageQueue.add(message);
    }
}