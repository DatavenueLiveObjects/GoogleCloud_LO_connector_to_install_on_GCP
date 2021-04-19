/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.threeten.bp.Duration;

import java.io.IOException;

@Configuration
public class PubSubConfig {

    private final PubSubProperties pubSubProperties;

    public PubSubConfig(PubSubProperties pubSubProperties) {
        this.pubSubProperties = pubSubProperties;
    }

    @Bean
    public Publisher publisher() throws IOException {
        TopicName topicName = TopicName.of(pubSubProperties.getProjectId(), pubSubProperties.getTopicId());

        GoogleCredentials googleCredentials = getCredentials();

        BatchingSettings batchingSettings = getBatchSettings();

        RetrySettings retrySettings = getRetrySettings();

        return Publisher.newBuilder(topicName)
                .setCredentialsProvider(FixedCredentialsProvider.create(googleCredentials))
                .setBatchingSettings(batchingSettings)
                .setRetrySettings(retrySettings)
                .build();
    }

    private RetrySettings getRetrySettings() {
        return RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(pubSubProperties.getInitialRetryDelay()))
                .setRetryDelayMultiplier(pubSubProperties.getRetryDelayMultiplier())
                .setMaxRetryDelay(Duration.ofMillis(pubSubProperties.getMaxRetryDelay()))
                .setInitialRpcTimeout(Duration.ofMillis(pubSubProperties.getInitialRpcTimeout()))
                .setRpcTimeoutMultiplier(pubSubProperties.getRpcTimeoutMultiplier())
                .setMaxRpcTimeout(Duration.ofMillis(pubSubProperties.getMaxRpcTimeout()))
                .setTotalTimeout(Duration.ofMillis(pubSubProperties.getTotalTimeout()))
                .build();
    }

    private BatchingSettings getBatchSettings() {
        return BatchingSettings.newBuilder()
                .setElementCountThreshold(pubSubProperties.getMessageBatchSize())
                .setRequestByteThreshold(Publisher.getApiMaxRequestBytes())
                .setDelayThreshold(Duration.ofMillis(pubSubProperties.getMessageSendingFixedDelay()))
                .build();
    }

    private GoogleCredentials getCredentials() throws IOException {
        return GoogleCredentials
                .fromStream(new ClassPathResource(pubSubProperties.getAuthFile()).getInputStream());
    }
}
