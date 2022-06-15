/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub;

import com.orange.lo.sample.lo2pubsub.liveobjects.LoProperties;
import com.orange.lo.sample.lo2pubsub.pubsub.PubSubProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({LoProperties.class, PubSubProperties.class})
public class ConnectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConnectorApplication.class, args);
    }
}