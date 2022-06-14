/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orange.lo.sample.lo2pubsub.utils.Counters;

import io.micrometer.core.instrument.Counter;

@ExtendWith(MockitoExtension.class)
class LoMqttMessageHandlerTest {

    @Mock
    private Counters counters;

    @Mock
    private Counter receivedCounter;

    private Queue<String> messageQueue;

    private LoMqttMessageHandler handler;

    @BeforeEach
    void setUp() {
        when(counters.getMesasageReadCounter()).thenReturn(receivedCounter);
        messageQueue = new LinkedList<>();
        handler = new LoMqttMessageHandler(counters, messageQueue);
    }

    @Test
    public void shouldIncrementCounterOnMessage() {
        // when
        handler.onMessage("test message");

        // then
        verify(receivedCounter, times(1)).increment();
    }

    @Test
    public void shouldAddMessageToQueueOnMessage() {
        // when
        handler.onMessage("test message");

        // then
        assertEquals(1, messageQueue.size());
        assertEquals("test message", messageQueue.peek());
    }

}