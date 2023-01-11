/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
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

    public PubSubProperties(
            String projectId,
            String topicId,
            String authFile,
            Long messageBatchSize,
            Integer messageSendingFixedDelay,
            Long initialRetryDelay,
            Double retryDelayMultiplier,
            Long maxRetryDelay,
            Long initialRpcTimeout,
            Double rpcTimeoutMultiplier,
            Long maxRpcTimeout,
            Long totalTimeout
    ) {
        this.projectId = projectId;
        this.topicId = topicId;
        this.authFile = authFile;
        this.messageBatchSize = messageBatchSize;
        this.messageSendingFixedDelay = messageSendingFixedDelay;
        this.initialRetryDelay = initialRetryDelay;
        this.retryDelayMultiplier = retryDelayMultiplier;
        this.maxRetryDelay = maxRetryDelay;
        this.initialRpcTimeout = initialRpcTimeout;
        this.rpcTimeoutMultiplier = rpcTimeoutMultiplier;
        this.maxRpcTimeout = maxRpcTimeout;
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