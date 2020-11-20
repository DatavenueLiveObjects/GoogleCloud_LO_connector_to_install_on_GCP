package com.orange.lo.sample.lo2pubsub.liveobjects;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LOPropertiesTest {

    @Test
    void shouldSetHostnameCorrectly() {
        String hostname = "liveobjects.orange-business.com";
        LOProperties loProperties = new LOProperties();
        loProperties.setHostname(hostname);

        assertEquals(hostname, loProperties.getHostname());
    }

    @Test
    void shouldSetUsernameCorrectly() {
        String username = "application";
        LOProperties loProperties = new LOProperties();
        loProperties.setUsername(username);

        assertEquals(username, loProperties.getUsername());
    }

    @Test
    void shouldSetApiKeyCorrectly() {
        String apiKey = "loAp1K3y";
        LOProperties loProperties = new LOProperties();
        loProperties.setApiKey(apiKey);

        assertEquals(apiKey, loProperties.getApiKey());
    }

    @Test
    void shouldSetTopicsCorrectly() {
        List<String> topics = Arrays.asList("topic-01", "topic-02");
        LOProperties loProperties = new LOProperties();
        loProperties.setTopics(topics);

        assertEquals(topics, loProperties.getTopics());
    }

    @Test
    void shouldSetRecoveryIntervalCorrectly() {
        int recoveryInterval = 1000;
        LOProperties loProperties = new LOProperties();
        loProperties.setRecoveryInterval(recoveryInterval);

        assertEquals(recoveryInterval, loProperties.getRecoveryInterval());
    }

    @Test
    void shouldSetCompletionTimeoutCorrectly() {
        int completionTimeout = 2000;
        LOProperties loProperties = new LOProperties();
        loProperties.setCompletionTimeout(completionTimeout);

        assertEquals(completionTimeout, loProperties.getCompletionTimeout());
    }

    @Test
    void shouldSetConnectionTimeoutCorrectly() {
        int connectionTimeout = 3000;
        LOProperties loProperties = new LOProperties();
        loProperties.setConnectionTimeout(connectionTimeout);

        assertEquals(connectionTimeout, loProperties.getConnectionTimeout());
    }

    @Test
    void shouldSetQosCorrectly() {
        int qos = 1;
        LOProperties loProperties = new LOProperties();
        loProperties.setQos(qos);

        assertEquals(qos, loProperties.getQos());
    }

    @Test
    void shouldSetKeepAliveIntervalSecondsCorrectly() {
        int keepAliveInterval = 4000;
        LOProperties loProperties = new LOProperties();
        loProperties.setKeepAliveIntervalSeconds(keepAliveInterval);

        assertEquals(keepAliveInterval, loProperties.getKeepAliveIntervalSeconds());
    }
}