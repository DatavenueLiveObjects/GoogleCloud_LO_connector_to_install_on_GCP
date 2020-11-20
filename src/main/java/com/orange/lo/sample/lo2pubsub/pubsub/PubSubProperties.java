/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.pub-sub")
public class PubSubProperties {

	private String projectId;
	private String topicId;
	private String authFile;
	private long messageBatchSize;
	private int messageSendingFixedDelay;
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getAuthFile() {
		return authFile;
	}
	public void setAuthFile(String authFile) {
		this.authFile = authFile;
	}
	public long getMessageBatchSize() {
		return messageBatchSize;
	}
	public void setMessageBatchSize(long messageBatchSize) {
		this.messageBatchSize = messageBatchSize;
	}
	public int getMessageSendingFixedDelay() {
		return messageSendingFixedDelay;
	}
	public void setMessageSendingFixedDelay(int messageSendingFixedDelay) {
		this.messageSendingFixedDelay = messageSendingFixedDelay;
	}	 
}