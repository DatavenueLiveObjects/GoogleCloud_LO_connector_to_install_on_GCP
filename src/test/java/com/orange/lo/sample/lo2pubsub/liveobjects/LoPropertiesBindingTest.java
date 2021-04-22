/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(LoProperties.class)
@TestPropertySource(value = "classpath:application.yaml")
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class LoPropertiesBindingTest {

    @Autowired
    private LoProperties loProperties;

    @Test
    public void shouldBindLoPropertiesFromYamlConfiguration() {
        Assertions.assertEquals("test-hostname", loProperties.getHostname());
        Assertions.assertEquals("test-api-key", loProperties.getApiKey());
        Assertions.assertEquals(Arrays.asList("test-topic1", "test-topic2"), loProperties.getTopics());
        Assertions.assertEquals(123, loProperties.getSynchronizationInterval());
        Assertions.assertEquals(234, loProperties.getMessageQos());
        Assertions.assertEquals("/tmp/test", loProperties.getMqttPersistenceDir());
        Assertions.assertEquals(345, loProperties.getKeepAliveIntervalSeconds());
        Assertions.assertEquals(456, loProperties.getConnectionTimeout());
        Assertions.assertEquals(567, loProperties.getMessageBatchSize());
        Assertions.assertEquals(678, loProperties.getTaskQueueSize());
        Assertions.assertEquals(789, loProperties.getThreadPoolSize());
        Assertions.assertEquals(890, loProperties.getKeepAliveTime());
    }

}
