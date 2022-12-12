/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lo")
public class LoProperties {

	private static final String CONNECTOR_TYPE = "LO_GCP_PUBSUB_ADAPTER";
	
    private String hostname;
    private String apiKey;
    private List<String> topics;
    private Integer synchronizationInterval;
    private Integer messageQos;
    private String mqttPersistenceDir;
    private Integer keepAliveIntervalSeconds;
    private Integer connectionTimeout;
    private Integer messageBatchSize;
    private Integer taskQueueSize;
    private Integer threadPoolSize;
    private Long keepAliveTime;
    
    public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	public void setSynchronizationInterval(Integer synchronizationInterval) {
		this.synchronizationInterval = synchronizationInterval;
	}

	public void setMessageQos(Integer messageQos) {
		this.messageQos = messageQos;
	}

	public void setMqttPersistenceDir(String mqttPersistenceDir) {
		this.mqttPersistenceDir = mqttPersistenceDir;
	}

	public void setKeepAliveIntervalSeconds(Integer keepAliveIntervalSeconds) {
		this.keepAliveIntervalSeconds = keepAliveIntervalSeconds;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setMessageBatchSize(Integer messageBatchSize) {
		this.messageBatchSize = messageBatchSize;
	}

	public void setTaskQueueSize(Integer taskQueueSize) {
		this.taskQueueSize = taskQueueSize;
	}

	public void setThreadPoolSize(Integer threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public void setKeepAliveTime(Long keepAliveTime) {
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