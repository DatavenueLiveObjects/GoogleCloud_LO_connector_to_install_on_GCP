/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.orange.lo.sample.lo2pubsub.pubsub.PubSubMessageSender;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

class SynchronizationTask implements Callable<Void> {

    private final PubSubMessageSender pubSubMessageSender;
    private final List<LoMessage> messageList;

    public SynchronizationTask(PubSubMessageSender pubSubMessageSender, List<LoMessage> messageList) {
        this.pubSubMessageSender = pubSubMessageSender;
        this.messageList = messageList;
    }

    @Override
    public Void call() {
        pubSubMessageSender.sendMessages(messageList);
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SynchronizationTask that = (SynchronizationTask) o;
        return Objects.equals(pubSubMessageSender, that.pubSubMessageSender) &&
                Objects.equals(messageList, that.messageList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pubSubMessageSender, messageList);
    }
}
