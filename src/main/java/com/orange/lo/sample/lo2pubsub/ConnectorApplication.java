/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub;

import com.orange.lo.sample.lo2pubsub.liveobjects.LoProperties;
import com.orange.lo.sample.lo2pubsub.pubsub.PubSubProperties;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({LoProperties.class, PubSubProperties.class})
public class ConnectorApplication {

	private static ConfigurableApplicationContext context;
    
    public static void main(String[] args) {
        context = SpringApplication.run(ConnectorApplication.class, args);
    }
    
    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(ConnectorApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}