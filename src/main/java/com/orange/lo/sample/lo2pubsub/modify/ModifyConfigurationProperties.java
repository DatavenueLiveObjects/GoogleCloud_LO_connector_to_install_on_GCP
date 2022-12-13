package com.orange.lo.sample.lo2pubsub.modify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyConfigurationProperties {

    private String loApiKey;
    private List<String> loTopics = new ArrayList<>();
    private String pubSubProjectId;
    private String pubSubTopicId;
    
    public String getPubSubTopicId() {
		return pubSubTopicId;
	}
	public void setPubSubTopicId(String pubSubTopicId) {
		this.pubSubTopicId = pubSubTopicId;
	}
	private String pubSubAuthFileContent;
	
    public String getLoApiKey() {
		return loApiKey;
	}
	public void setLoApiKey(String loApiKey) {
		this.loApiKey = loApiKey;
	}
	public List<String> getLoTopics() {
		return loTopics;
	}
	public void setLoTopics(List<String> loTopics) {
		this.loTopics = loTopics;
	}
	public String getPubSubProjectId() {
		return pubSubProjectId;
	}
	public void setPubSubProjectId(String pubSubProjectId) {
		this.pubSubProjectId = pubSubProjectId;
	}
	public String getPubSubAuthFileContent() {
		return pubSubAuthFileContent;
	}
	public void setPubSubAuthFileContent(String pubSubAuthFileContent) {
		this.pubSubAuthFileContent = pubSubAuthFileContent;
	}
	@Override
	public String toString() {
		return "ModifyConfigurationProperties [loApiKey=***" + ", loTopics=" + loTopics + ", pubSubProjectId="
				+ pubSubProjectId + ", pubSubTopicId=" + pubSubTopicId + ", pubSubAuthFileContent="
				+ pubSubAuthFileContent + "]";
	}
	
	
}