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
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.orange.lo.sample.lo2pubsub.utils.CountersProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class PubSubMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Publisher publisher;
    private final CountersProvider countersProvider;
    private final ApiFuturesCallbackSupport apiFuturesCallbackSupport;

    public PubSubMessageSender(
            Publisher publisher,
            CountersProvider countersProvider,
            ApiFuturesCallbackSupport apiFuturesCallbackSupport
    ) {
        this.publisher = publisher;
        this.countersProvider = countersProvider;
        this.apiFuturesCallbackSupport = apiFuturesCallbackSupport;
    }

    public void sendMessages(List<String> messageList) {
        messageList.forEach(this::sendMessage);
    }

    private void sendMessage(String message) {
        countersProvider.evtAttempt().increment();

        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage
                .newBuilder()
                .setData(data)
                .build();
        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

        apiFuturesCallbackSupport.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Unable to publish message ", throwable);
                countersProvider.evtFailed().increment();
            }

            @Override
            public void onSuccess(String messageId) {
                LOGGER.debug("Message {} published", messageId);
                countersProvider.evtSent().increment();
            }
        });
    }
}
