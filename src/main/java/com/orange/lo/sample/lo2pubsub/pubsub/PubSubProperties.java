/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.pub-sub")
public class PubSubProperties {

    private String projectId;
    private String topicId;

    private String authFile;

    private Long messageBatchSize;
    private Integer messageSendingFixedDelay;

    private Long initialRetryDelay;
    private Double retryDelayMultiplier;
    private Long maxRetryDelay;
    private Long initialRpcTimeout;
    private Double rpcTimeoutMultiplier;
    private Long maxRpcTimeout;
    private Long totalTimeout;

    public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public void setAuthFile(String authFile) {
		this.authFile = authFile;
	}

	public void setMessageBatchSize(Long messageBatchSize) {
		this.messageBatchSize = messageBatchSize;
	}

	public void setMessageSendingFixedDelay(Integer messageSendingFixedDelay) {
		this.messageSendingFixedDelay = messageSendingFixedDelay;
	}

	public void setInitialRetryDelay(Long initialRetryDelay) {
		this.initialRetryDelay = initialRetryDelay;
	}

	public void setRetryDelayMultiplier(Double retryDelayMultiplier) {
		this.retryDelayMultiplier = retryDelayMultiplier;
	}

	public void setMaxRetryDelay(Long maxRetryDelay) {
		this.maxRetryDelay = maxRetryDelay;
	}

	public void setInitialRpcTimeout(Long initialRpcTimeout) {
		this.initialRpcTimeout = initialRpcTimeout;
	}

	public void setRpcTimeoutMultiplier(Double rpcTimeoutMultiplier) {
		this.rpcTimeoutMultiplier = rpcTimeoutMultiplier;
	}

	public void setMaxRpcTimeout(Long maxRpcTimeout) {
		this.maxRpcTimeout = maxRpcTimeout;
	}

	public void setTotalTimeout(Long totalTimeout) {
		this.totalTimeout = totalTimeout;
	}

	public String getProjectId() {
        return projectId;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getAuthFile() {
        return authFile;
    }

    public Long getMessageBatchSize() {
        return messageBatchSize;
    }

    public Integer getMessageSendingFixedDelay() {
        return messageSendingFixedDelay;
    }

    public Long getInitialRetryDelay() {
        return initialRetryDelay;
    }

    public Double getRetryDelayMultiplier() {
        return retryDelayMultiplier;
    }

    public Long getMaxRetryDelay() {
        return maxRetryDelay;
    }

    public Long getInitialRpcTimeout() {
        return initialRpcTimeout;
    }

    public Double getRpcTimeoutMultiplier() {
        return rpcTimeoutMultiplier;
    }

    public Long getMaxRpcTimeout() {
        return maxRpcTimeout;
    }

    public Long getTotalTimeout() {
        return totalTimeout;
    }
}