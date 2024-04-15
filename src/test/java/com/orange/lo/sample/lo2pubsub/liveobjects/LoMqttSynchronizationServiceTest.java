/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.orange.lo.sample.lo2pubsub.pubsub.PubSubMessageSender;
import com.orange.lo.sample.lo2pubsub.utils.ConnectorHealthActuatorEndpoint;
import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.fifomqtt.DataManagementFifo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoMqttSynchronizationServiceTest {

    @Mock
    LOApiClient loApiClient;

    @Mock
    PubSubMessageSender pubSubMessageSender;

    @Mock
    DataManagementFifo dataManagementFifo;

    @Mock
    private LoProperties properties;

    @Mock
    private ThreadPoolExecutor threadPoolExecutor;

    @Mock
    private ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint;

    private LoMqttSynchronizationService service;


    @BeforeEach
    void setUp() {
        when(loApiClient.getDataManagementFifo()).thenReturn(dataManagementFifo);
        prepareService(new LinkedList<>());
    }

    private void prepareService(Queue<LoMessage> messageQueue) {
        service = new LoMqttSynchronizationService(
                loApiClient, pubSubMessageSender, messageQueue, properties, threadPoolExecutor, connectorHealthActuatorEndpoint
        );
    }

    @Test
    public void shouldStartMethodDoTriggerDataManagementFifo() {
        // when
        service.start();

        // then
        verify(dataManagementFifo, times(1)).connect();
    }

    @Test
    public void shouldStopMethodDoDisconnectDataManagementFifo() {
        // when
        service.stop();

        // then
        verify(dataManagementFifo, times(1)).disconnect();
    }

    @Test
    public void shouldNotSendMessagesIfQueueIsEmpty() {
        // when
        service.synchronize();

        // then
        verify(pubSubMessageSender, never()).sendMessages(any());
    }

    @Test
    public void shouldExecuteMessageSynchronizationTaskInOneBatchIfQueueNotExceedMessageBatchSizeProperty() throws InterruptedException {
        // given
        int batchSize = 5;

        when(properties.getMessageBatchSize()).thenReturn(batchSize);

        Queue<LoMessage> messageQueue = getExampleMessageQueue(batchSize);
        List<LoMessage> expectedMessages = new ArrayList<>(messageQueue);

        prepareService(messageQueue);

        // when
        service.synchronize();

        // then

        List<SynchronizationTask> expectedTasks = Collections.singletonList(
                new SynchronizationTask(pubSubMessageSender, expectedMessages)
        );
        verify(threadPoolExecutor, times(1)).invokeAll(expectedTasks);
    }

    @Test
    public void shouldSplitMessagesIntoPacketsIfQueueExceedMessageBatchSizeProperty() throws InterruptedException {
        // given
        int batchSize = 5;
        int totalLength = batchSize + 1;

        when(properties.getMessageBatchSize()).thenReturn(batchSize);

        Queue<LoMessage> messageQueue = getExampleMessageQueue(totalLength);

        List<LoMessage> expectedMessages1 = (new LinkedList<>(messageQueue)).subList(0, batchSize);
        List<LoMessage> expectedMessages2 = (new LinkedList<>(messageQueue)).subList(batchSize, totalLength);

        prepareService(messageQueue);

        // when
        service.synchronize();

        // then
        List<SynchronizationTask> expectedTasks = Arrays.asList(
                new SynchronizationTask(pubSubMessageSender, expectedMessages1),
                new SynchronizationTask(pubSubMessageSender, expectedMessages2)
        );

        verify(threadPoolExecutor, times(1)).invokeAll(expectedTasks);
    }

    @Test
    public void shouldSetDefaultBatchSizeTo10() throws InterruptedException {
        // given
        when(properties.getMessageBatchSize()).thenReturn(null);

        int expectedBatchSize = 10;

        Queue<LoMessage> messageQueue = getExampleMessageQueue(expectedBatchSize);
        List<LoMessage> expectedMessages = (new LinkedList<>(messageQueue)).subList(0, expectedBatchSize);

        prepareService(messageQueue);

        // when
        service.synchronize();

        // then
        List<SynchronizationTask> expectedTasks = Collections.singletonList(
                new SynchronizationTask(pubSubMessageSender, expectedMessages)
        );
        verify(threadPoolExecutor, times(1)).invokeAll(expectedTasks);
        verify(threadPoolExecutor, never()).invokeAll(not(eq(expectedTasks)));
    }

    @Test
    public void shouldClearQueueAfterMessagesSend() throws InterruptedException {
        // given
        int batchSize = 5;

        when(properties.getMessageBatchSize()).thenReturn(batchSize);

        Queue<LoMessage> messageQueue = getExampleMessageQueue(batchSize);

        prepareService(messageQueue);

        // when
        service.synchronize();

        // then
        assertEquals(0, messageQueue.size());
    }

    private Queue<LoMessage> getExampleMessageQueue(int batchSize) {
        return IntStream.range(1, batchSize + 1)
                .mapToObj(i -> new LoMessage(i, String.format("Message %d", i)))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}