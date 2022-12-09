package com.orange.lo.sample.lo2pubsub.modify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyConfigurationProperties {

    private String loApiKey;
    private List<String> topics = new ArrayList<>();
    private String projectId;
    private String topicId;
    private String authFileContent;

    public String getLoApiKey() {
        return loApiKey;
    }

    public void setLoApiKey(String loApiKey) {
        this.loApiKey = loApiKey;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

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

    public String getAuthFileContent() {
        return authFileContent;
    }

    public void setAuthFileContent(String authFileContent) {
        this.authFileContent = authFileContent;
    }

    @Override
    public String toString() {
        return "ModifyConfigurationProperties{" +
                "loApiKey='" + loApiKey + '\'' +
                ", topics=" + topics +
                ", projectId='" + projectId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", authFileContent='" + authFileContent + '\'' +
                '}';
    }
}