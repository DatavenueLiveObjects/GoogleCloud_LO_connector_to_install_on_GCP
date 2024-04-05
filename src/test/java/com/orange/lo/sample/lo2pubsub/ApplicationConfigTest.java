/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub;

import com.orange.lo.sample.lo2pubsub.liveobjects.LoMessage;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationConfigTest {

    @Test
    void shouldCreateMessageQueue() {
        ApplicationConfig applicationConfig = new ApplicationConfig();

        Queue<LoMessage> stringQueue = applicationConfig.messageQueue();

        assertNotNull(stringQueue);
    }
}