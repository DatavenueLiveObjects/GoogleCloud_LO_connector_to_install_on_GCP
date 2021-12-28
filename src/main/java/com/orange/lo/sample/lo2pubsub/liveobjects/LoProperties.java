/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@ConfigurationProperties(prefix = "lo")
public class LoProperties {

	private static final String CONNECTOR_TYPE = "LO_GCP_PUBSUB_ADAPTER";
	
    private final String hostname;
    private final String apiKey;
    private final List<String> topics;
    private final Integer synchronizationInterval;
    private final Integer messageQos;
    private final String mqttPersistenceDir;
    private final Integer keepAliveIntervalSeconds;
    private final Integer connectionTimeout;
    private final Integer messageBatchSize;
    private final Integer taskQueueSize;
    private final Integer threadPoolSize;
    private final Long keepAliveTime;

    public LoProperties(
            String hostname,
            String apiKey,
            List<String> topics,
            Integer synchronizationInterval,
            Integer messageQos,
            String mqttPersistenceDir,
            Integer keepAliveIntervalSeconds,
            Integer connectionTimeout,
            Integer messageBatchSize,
            Integer taskQueueSize,
            Integer threadPoolSize,
            Long keepAliveTime
    ) {
        this.hostname = hostname;
        this.apiKey = apiKey;
        this.topics = topics;
        this.synchronizationInterval = synchronizationInterval;
        this.messageQos = messageQos;
        this.mqttPersistenceDir = mqttPersistenceDir;
        this.keepAliveIntervalSeconds = keepAliveIntervalSeconds;
        this.connectionTimeout = connectionTimeout;
        this.messageBatchSize = messageBatchSize;
        this.taskQueueSize = taskQueueSize;
        this.threadPoolSize = threadPoolSize;
        this.keepAliveTime = keepAliveTime;
    }
    
    public String getConnectorType() {
        return CONNECTOR_TYPE;
    }

    public String getHostname() {
        return hostname;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<String> getTopics() {
        return topics;
    }

    public Integer getSynchronizationInterval() {
        return synchronizationInterval;
    }

    public Integer getMessageQos() {
        return messageQos;
    }

    public String getMqttPersistenceDir() {
        return mqttPersistenceDir;
    }

    public Integer getKeepAliveIntervalSeconds() {
        return keepAliveIntervalSeconds;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public Integer getMessageBatchSize() {
        return messageBatchSize;
    }

    public Integer getTaskQueueSize() {
        return taskQueueSize;
    }

    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }
}