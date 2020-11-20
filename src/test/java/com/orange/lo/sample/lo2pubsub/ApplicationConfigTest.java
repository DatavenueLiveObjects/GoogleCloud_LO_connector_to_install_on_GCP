package com.orange.lo.sample.lo2pubsub;

import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigTest {

    @Test
    void shouldCreateMessageQueue() {
        ApplicationConfig applicationConfig = new ApplicationConfig();

        Queue<String> stringQueue = applicationConfig.messageQueue();

        assertNotNull(stringQueue);
    }
}