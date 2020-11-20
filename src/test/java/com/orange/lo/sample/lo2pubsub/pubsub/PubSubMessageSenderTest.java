package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import com.orange.lo.sample.lo2pubsub.utils.Counters;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PubSubMessageSenderTest {

    @Mock
    private Publisher publisher;
    @Mock
    private Counters counters;
    @Mock
    private Counter counter;
    @Mock
    private Counter failedCounter;
    @Mock
    private Counter sentCounter;
    private PubSubMessageSender pubSubMessageSender;

    @BeforeEach
    void setUp() {
        when(counters.evtAttempt()).thenReturn(counter);
    }

    @Test
    void shouldCorrectlyHandleMessageWhenUnableToPublishMessage() {
        when(counters.evtFailed()).thenReturn(failedCounter);
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(ApiFutures.immediateFailedFuture(new IOException()));

        pubSubMessageSender = new PubSubMessageSender(publisher, counters);
        pubSubMessageSender.handleMessage("{}");

        verify(counter, times(1)).increment();
        verify(publisher, times(1)).publish(any(PubsubMessage.class));
        verify(failedCounter, times(1)).increment();
    }

    @Test
    void shouldCorrectlyHandleMessageWhenMessageWasPublished() {
        when(counters.evtSent()).thenReturn(sentCounter);
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(ApiFutures.immediateFuture("message-Id"));

        pubSubMessageSender = new PubSubMessageSender(publisher, counters);
        pubSubMessageSender.handleMessage("{}");

        verify(counter, times(1)).increment();
        verify(publisher, times(1)).publish(any(PubsubMessage.class));
        verify(sentCounter, times(1)).increment();
    }
}