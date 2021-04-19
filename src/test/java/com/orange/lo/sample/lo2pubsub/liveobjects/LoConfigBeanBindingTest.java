/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.google.cloud.pubsub.v1.Publisher;
import com.orange.lo.sdk.LOApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LoConfigBeanBindingTest {

    @Autowired
    private LOApiClient loApiClient;

    @MockBean
    private LoMqttSynchronizationService loMqttSynchronizationService;

    @MockBean
    private Publisher publisher;

    @Test
    public void shouldAllBeansProperlyBound() {
        assertNotNull(loApiClient);
    }
}