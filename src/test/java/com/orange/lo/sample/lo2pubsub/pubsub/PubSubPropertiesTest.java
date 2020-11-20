package com.orange.lo.sample.lo2pubsub.pubsub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PubSubPropertiesTest {

    private PubSubProperties pubSubProperties;

    @BeforeEach
    void setUp() {
        pubSubProperties = new PubSubProperties();
    }

    @Test
    void shouldSetProjectId() {
        String projectId = "pub-sub-project-id";
        pubSubProperties.setProjectId(projectId);

        assertEquals(projectId, pubSubProperties.getProjectId());
    }

    @Test
    void shouldSetTopicId() {
        String topicId = "pub-sub-topic-id";
        pubSubProperties.setTopicId(topicId);

        assertEquals(topicId, pubSubProperties.getTopicId());
    }

    @Test
    void shouldSetAuthFile() {
        String authFile = "credentials/auth-file.json";
        pubSubProperties.setAuthFile(authFile);

        assertEquals(authFile, pubSubProperties.getAuthFile());
    }

    @Test
    void shouldSetMessageBatchSize() {
        long messageBatchSize = 10L;
        pubSubProperties.setMessageBatchSize(messageBatchSize);

        assertEquals(messageBatchSize, pubSubProperties.getMessageBatchSize());
    }

    @Test
    void shouldSetMessageSendingFixedDelay() {
        int messageSendingFixedDelay = 1000;
        pubSubProperties.setMessageSendingFixedDelay(messageSendingFixedDelay);

        assertEquals(messageSendingFixedDelay, pubSubProperties.getMessageSendingFixedDelay());
    }
}