package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.orange.lo.sample.lo2pubsub.utils.CountersProvider;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoMqttMessageHandlerTest {

    @Mock
    private CountersProvider countersProvider;

    @Mock
    private Counter receivedCounter;

    private Queue<String> messageQueue;

    private LoMqttMessageHandler handler;

    @BeforeEach
    void setUp() {
        when(countersProvider.evtReceived()).thenReturn(receivedCounter);
        messageQueue = new LinkedList<>();
        handler = new LoMqttMessageHandler(countersProvider, messageQueue);
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