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
import com.google.pubsub.v1.PubsubMessage;
import com.orange.lo.sample.lo2pubsub.liveobjects.LoMessage;
import com.orange.lo.sample.lo2pubsub.liveobjects.LoProperties;
import com.orange.lo.sample.lo2pubsub.utils.ConnectorHealthActuatorEndpoint;
import com.orange.lo.sample.lo2pubsub.utils.Counters;
import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.fifomqtt.DataManagementFifo;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PubSubMessageSenderTest {

    @Mock
    private Publisher publisher;

    @Mock
    private Counters counters;

    @Mock
    private Counter attemptCounter;

    @Mock
    private Counter failedCounter;

    @Mock
    private Counter sentCounter;

    @Mock
    private ApiFuture<String> apiFuture;

    @Mock
    LOApiClient loApiClient;

    @Mock
    DataManagementFifo dataManagementFifo;

    @Mock
    LoProperties loProperties;

    @Mock
    private ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint;

    private ApiFuturesCallbackHandler futuresHandler;

    private PubSubMessageSender pubSubMessageSender;

    CheckConnectionApiFutureCallbackImpl apiFutureCallback;

    @BeforeEach
    void setUp() {
        when(counters.getMesasageSentAttemptCounter()).thenReturn(attemptCounter);

        futuresHandler = new ApiFuturesCallbackHandler();

        pubSubMessageSender = new PubSubMessageSender(
                publisher, counters, futuresHandler, loApiClient, loProperties,
                connectorHealthActuatorEndpoint, apiFutureCallback
        );
    }

    @Test
    void shouldSendMessagesWithPublisher() {
        // given
        when(publisher.publish(any())).thenReturn(apiFuture);

        // when
        List<LoMessage> exampleMessagesList = Arrays.asList(new LoMessage(1, "message1"), new LoMessage(2, "message2"));
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
                publisher, counters, futuresHandler, loApiClient, loProperties,
                connectorHealthActuatorEndpoint, apiFutureCallback
        );

        // when
        List<LoMessage> exampleMessagesList = Arrays.asList(new LoMessage(1, "message1"), new LoMessage(2, "message2"));
        pubSubMessageSender.sendMessages(exampleMessagesList);

        // then
        verify(futuresHandler, times(2)).addCallback(eq(apiFuture), notNull());
    }

    @Test
    void shouldIncrementSentCounterAfterSuccess() {
        // given
        when(counters.getMesasageSentCounter()).thenReturn(sentCounter);
        when(loApiClient.getDataManagementFifo()).thenReturn(dataManagementFifo);

        when(publisher.publish(any())).thenReturn(apiFuture);

        List<LoMessage> exampleMessagesList = Arrays.asList(new LoMessage(1, "message1"), new LoMessage(2, "message2"));
        pubSubMessageSender.sendMessages(exampleMessagesList);

        // when
        futuresHandler.getHandledCallback().onSuccess("message");

        // then
        verify(sentCounter, times(1)).increment();
    }

    @Test
    void shouldIncrementFailedCounterAfterSuccess() {
        // given
        when(counters.getMesasageSentFailedCounter()).thenReturn(failedCounter);
        when(loApiClient.getDataManagementFifo()).thenReturn(dataManagementFifo);

        when(publisher.publish(any())).thenReturn(apiFuture);

        List<LoMessage> exampleMessagesList = Arrays.asList(new LoMessage(1, "message1"), new LoMessage(2, "message2"));
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