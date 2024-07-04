/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.google.cloud.pubsub.v1.Publisher;
import com.orange.lo.sample.lo2pubsub.pubsub.ApiFuturesCallbackSupport;
import com.orange.lo.sdk.LOApiClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LoConfigBeanBindingTest {

    @TempDir
    static File tempDir;

    static {
        File config = new File(tempDir, "config");
        System.setProperty("aws.region", "eu-west-1");
        System.setProperty("aws.profile", "service-profile");
        System.setProperty("aws.configFile", config.getAbsolutePath());
        List<String> lines = Arrays.asList("[profile service-profile]", "region=eu-west-1");
        try {
            Files.write(config.toPath(), lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private LOApiClient loApiClient;

    @MockBean
    private LoMqttSynchronizationService loMqttSynchronizationService;

    @MockBean
    private Publisher publisher;
    @MockBean
    private ApiFuturesCallbackSupport apiFuturesCallbackSupport;

    @Test
    public void shouldAllBeansProperlyBound() {
        assertNotNull(loApiClient);
    }
}