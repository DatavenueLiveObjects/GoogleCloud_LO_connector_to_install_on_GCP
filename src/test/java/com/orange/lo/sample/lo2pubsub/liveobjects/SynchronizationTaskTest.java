/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.orange.lo.sample.lo2pubsub.pubsub.PubSubMessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SynchronizationTaskTest {

    @Mock
    PubSubMessageSender pubSubMessageSender;

    @Test
    public void shouldSendMessagesOnCall() {
        // given
        List<LoMessage> exampleMessagesList = Arrays.asList(new LoMessage(1, "message1") , new LoMessage(1, "message2"));
        SynchronizationTask synchronizationTask = new SynchronizationTask(pubSubMessageSender, exampleMessagesList);

        // when
        synchronizationTask.call();

        // then
        verify(pubSubMessageSender, times(1)).sendMessages(exampleMessagesList);
    }


}