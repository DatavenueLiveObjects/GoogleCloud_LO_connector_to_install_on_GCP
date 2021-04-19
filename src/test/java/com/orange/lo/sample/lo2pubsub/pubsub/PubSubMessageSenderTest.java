package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import com.orange.lo.sample.lo2pubsub.utils.CountersProvider;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PubSubMessageSenderTest {

    @Mock
    private Publisher publisher;

    @Mock
    private CountersProvider countersProvider;

    @Mock
    private Counter attemptCounter;

    @Mock
    private Counter failedCounter;

    @Mock
    private Counter sentCounter;

    @Mock
    private ApiFuture<String> apiFuture;

    private ApiFuturesCallbackHandler futuresHandler;

    private PubSubMessageSender pubSubMessageSender;

    @BeforeEach
    void setUp() {
        when(countersProvider.evtAttempt()).thenReturn(attemptCounter);

        futuresHandler = new ApiFuturesCallbackHandler();

        pubSubMessageSender = new PubSubMessageSender(
                publisher, countersProvider, futuresHandler
        );
    }

    @Test
    void shouldSendMessagesWithPublisher() {
        // given
        when(publisher.publish(any())).thenReturn(apiFuture);

        // when
        List<String> exampleMessagesList = Arrays.asList("message1", "message2");
        pubSubMessageSender.sendMessages(exampleMessagesList);

        // then
        verify(publisher, times(2)).publish(any(PubsubMessage.class));
        verify(attemptCounter, times(2)).increment();
    }

    @Test
    void shouldAddApiFutureCallbackToEachMessage() {
        // given
        when(publisher.publish(any())).thenReturn(apiFuture);

        futuresHandler = mock(ApiFuturesCallbackHandler.class);

        pubSubMessageSender = new PubSubMessageSender(
                publisher, countersProvider, futuresHandler
        );

        // when
        List<String> exampleMessagesList = Arrays.asList("message1", "message2");
        pubSubMessageSender.sendMessages(exampleMessagesList);

        // then
        verify(futuresHandler, times(2)).addCallback(eq(apiFuture), notNull());
    }

    @Test
    void shouldIncrementSentCounterAfterSuccess() {
        // given
        when(countersProvider.evtSent()).thenReturn(sentCounter);

        when(publisher.publish(any())).thenReturn(apiFuture);

        List<String> exampleMessagesList = Collections.singletonList("message1");
        pubSubMessageSender.sendMessages(exampleMessagesList);

        // when
        futuresHandler.getHandledCallback().onSuccess("message");

        // then
        verify(sentCounter, times(1)).increment();
    }

    @Test
    void shouldIncrementFailedCounterAfterSuccess() {
        // given
        when(countersProvider.evtFailed()).thenReturn(failedCounter);

        when(publisher.publish(any())).thenReturn(apiFuture);

        List<String> exampleMessagesList = Collections.singletonList("message1");
        pubSubMessageSender.sendMessages(exampleMessagesList);

        // when
        futuresHandler.getHandledCallback().onFailure(new Exception("message"));

        // then
        verify(failedCounter, times(1)).increment();
    }

    static class ApiFuturesCallbackHandler extends ApiFuturesCallbackSupport {

        ApiFutureCallback<String> handledCallback;

        @Override
        public void addCallback(ApiFuture<String> future, ApiFutureCallback<String> callback) {
            handledCallback = callback;
        }

        public ApiFutureCallback<String> getHandledCallback() {
            return handledCallback;
        }
    }
}