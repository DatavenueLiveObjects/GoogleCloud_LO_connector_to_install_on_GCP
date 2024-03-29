/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.liveobjects;

import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.LOApiClientParameters;
import com.orange.lo.sdk.fifomqtt.DataManagementFifoCallback;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class LoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LoProperties loProperties;
    private final LOApiClientFactory loApiClientFactory;
    private final DataManagementFifoCallback dataManagementFifoCallback;

    public LoConfig(LoProperties loProperties, DataManagementFifoCallback dataManagementFifoCallback, LOApiClientFactory loApiClientFactory) {
        this.loProperties = loProperties;
        this.dataManagementFifoCallback = dataManagementFifoCallback;
        this.loApiClientFactory = loApiClientFactory;
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        BlockingQueue<Runnable> tasks = new ArrayBlockingQueue<>(loProperties.getTaskQueueSize());
        return new ThreadPoolExecutor(
                loProperties.getThreadPoolSize(),
                loProperties.getThreadPoolSize(),
                loProperties.getKeepAliveTime(),
                TimeUnit.SECONDS, tasks
        );
    }

    @Bean
    public LOApiClient loApiClient() {
        LOGGER.debug("Initializing LOApiClient");
        LOApiClientParameters parameters = getLoApiClientParameters();
        return loApiClientFactory.createLOApiClient(parameters);
    }

    private LOApiClientParameters getLoApiClientParameters() {
        if (loProperties.getTopics().isEmpty()) {
            throw new IllegalArgumentException("Topics are required");
        }

        LOApiClientParameters.LOApiClientParametersBuilder builder = LOApiClientParameters.builder()
                .apiKey(loProperties.getApiKey())
                .topics(loProperties.getTopics())
                .dataManagementMqttCallback(dataManagementFifoCallback::onMessage)
                .automaticReconnect(true);

        if (StringUtils.hasText(loProperties.getHostname())) {
            builder.hostname(loProperties.getHostname());
        }

        if (StringUtils.hasText(loProperties.getMqttPersistenceDir())) {
            builder.mqttPersistenceDataDir(loProperties.getMqttPersistenceDir());
        }

        if (loProperties.getMessageQos() != null) {
            builder.messageQos(loProperties.getMessageQos());
        }

        if (loProperties.getKeepAliveIntervalSeconds() != null) {
            builder.keepAliveIntervalSeconds(loProperties.getKeepAliveIntervalSeconds());
        }

        if (loProperties.getConnectionTimeout() != null) {
            builder.connectionTimeout(loProperties.getConnectionTimeout());
        }

        builder.connectorType(loProperties.getConnectorType());
        builder.connectorVersion(getConnectorVersion());
        
        return builder.build();
    }
    
    private String getConnectorVersion() {
    	MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = null;
        try {			
	        if ((new File("pom.xml")).exists()) {
	          model = reader.read(new FileReader("pom.xml"));
	        } else {
	          model = reader.read(
	            new InputStreamReader(
	            	LoConfig.class.getResourceAsStream(
	                "/META-INF/maven/com.orange.lo.sample.lo2pubsub/lo2pubsub/pom.xml"
	              )
	            )
	          );
	        }
	        return model.getVersion().replace(".", "_");
        } catch (Exception e) {
			return "";
		}
    }
}