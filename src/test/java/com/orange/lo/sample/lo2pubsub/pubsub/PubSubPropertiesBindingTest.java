package com.orange.lo.sample.lo2pubsub.pubsub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(PubSubProperties.class)
@TestPropertySource(value = "classpath:application.yaml")
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class PubSubPropertiesBindingTest {

    @Autowired
    private PubSubProperties pubSubProperties;

    @Test
    public void shouldBindLoPropertiesFromYamlConfiguration() {
        assertEquals("test-project", pubSubProperties.getProjectId());
        assertEquals("test-topic", pubSubProperties.getTopicId());
        assertEquals("test-auth-file.json", pubSubProperties.getAuthFile());
        assertEquals(123, pubSubProperties.getMessageBatchSize());
        assertEquals(234, pubSubProperties.getMessageSendingFixedDelay());

        assertEquals(345, pubSubProperties.getInitialRetryDelay());
        assertEquals(1.2, pubSubProperties.getRetryDelayMultiplier());
        assertEquals(456, pubSubProperties.getMaxRetryDelay());
        assertEquals(567, pubSubProperties.getInitialRpcTimeout());
        assertEquals(2.3, pubSubProperties.getRpcTimeoutMultiplier());
        assertEquals(678, pubSubProperties.getMaxRpcTimeout());
        assertEquals(789, pubSubProperties.getTotalTimeout());
    }

}