/**
 * Copyright (c) Orange, Inc. and its affiliates. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import java.lang.invoke.MethodHandles;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.orange.lo.sample.lo2pubsub.utils.Counters;
import com.orange.lo.sdk.fifomqtt.DataManagementFifoCallback;

@Component
public class LoMqttMessageHandler implements DataManagementFifoCallback {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Counters counters;
    private final Queue<LoMessage> messageQueue;

    public LoMqttMessageHandler(Counters counters, Queue<LoMessage> messageQueue) {
        LOG.info("LoMqttHandler init...");

        this.counters = counters;
        this.messageQueue = messageQueue;
    }

    @Override
    public void onMessage(int messageId, String message) {
        counters.getMesasageReadCounter().increment();
        messageQueue.add(new LoMessage(messageId, message));
    }
}