/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.threeten.bp.Duration;

import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;

@Configuration
public class PubSubConfig {
	
	private final PubSubProperties pubSubProperties;
	
	public PubSubConfig(PubSubProperties pubSubProperties) {
		this.pubSubProperties = pubSubProperties;
	}

	@Bean
	public Publisher publisher() throws IOException {		
		TopicName topicName = TopicName.of(pubSubProperties.getProjectId(), pubSubProperties.getTopicId());
		
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(pubSubProperties.getAuthFile()).getInputStream());
		
		BatchingSettings batchingSettings =
		          BatchingSettings.newBuilder()
		              .setElementCountThreshold(pubSubProperties.getMessageBatchSize())
		              .setRequestByteThreshold(Publisher.getApiMaxRequestBytes())
		              .setDelayThreshold(Duration.ofMillis(pubSubProperties.getMessageSendingFixedDelay()))
		              .build();
		
		
		return Publisher.newBuilder(topicName)
				.setCredentialsProvider(FixedCredentialsProvider.create(googleCredentials))
				.setBatchingSettings(batchingSettings)
				.build();
	}
}
