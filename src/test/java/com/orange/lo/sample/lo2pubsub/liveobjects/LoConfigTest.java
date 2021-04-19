package com.orange.lo.sample.lo2pubsub.liveobjects;


import com.orange.lo.sdk.LOApiClientParameters;
import com.orange.lo.sdk.fifomqtt.DataManagementFifoCallback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoConfigTest {

    public static final String API_KEY = "abcDEfgH123I";
    public static final List<String> TOPICS = Arrays.asList("topic-one", "topic-two");

    @Mock
    private DataManagementFifoCallback dataManagementFifoCallback;

    @Mock
    private LOApiClientFactory loApiClientFactory;

    @Mock
    private LoProperties properties;

    @Captor
    private ArgumentCaptor<LOApiClientParameters> clientParametersCaptor;

    @Test
    void shouldCreateLoApiClientBasedOnLoPropertiesWithDefaultValues() {
        // given
        LoConfig loConfig = new LoConfig(getMinimumLoProperties(), dataManagementFifoCallback, loApiClientFactory);

        // when
        loConfig.loApiClient();

        // then
        verify(loApiClientFactory).createLOApiClient(clientParametersCaptor.capture());
        LOApiClientParameters clientParameters = clientParametersCaptor.getValue();

        assertEquals(API_KEY, clientParameters.getApiKey());
        assertEquals(TOPICS, clientParameters.getTopics());

        assertNotNull(clientParameters.getHostname());
        assertNotNull(clientParameters.getMqttPersistenceDataDir());
        assertTrue(clientParameters.getMessageQos() > 0);
        assertTrue(clientParameters.getKeepAliveIntervalSeconds() > 0);
        assertTrue(clientParameters.getConnectionTimeout() > 0);
    }

    @Test
    void shouldThrowExceptionOnLoApiClientIfPropertiesHasNoTopic() {
        // given
        LoConfig loConfig = new LoConfig(properties, dataManagementFifoCallback, loApiClientFactory);

        // when / then
        assertThrows(IllegalArgumentException.class, loConfig::loApiClient);
    }

    @Test
    void shouldCreateLoApiClientBasedOnProvidedLoProperties() {
        // given
        LoProperties customizedLoProperties = getCustomizedLoProperties();
        LoConfig loConfig = new LoConfig(customizedLoProperties, dataManagementFifoCallback, loApiClientFactory);

        // when
        loConfig.loApiClient();

        // then
        verify(loApiClientFactory).createLOApiClient(clientParametersCaptor.capture());
        LOApiClientParameters clientParameters = clientParametersCaptor.getValue();

        assertEquals(API_KEY, clientParameters.getApiKey());
        assertEquals(TOPICS, clientParameters.getTopics());

        assertEquals(customizedLoProperties.getHostname(), clientParameters.getHostname());
        assertEquals(customizedLoProperties.getMqttPersistenceDir(), clientParameters.getMqttPersistenceDataDir());
        assertEquals((int) customizedLoProperties.getMessageQos(), clientParameters.getMessageQos());
        assertEquals((int) customizedLoProperties.getKeepAliveIntervalSeconds(), clientParameters.getKeepAliveIntervalSeconds());
        assertEquals((int) customizedLoProperties.getConnectionTimeout(), clientParameters.getConnectionTimeout());
    }

    private LoProperties getMinimumLoProperties() {
        when(properties.getApiKey()).thenReturn(API_KEY);
        when(properties.getTopics()).thenReturn(TOPICS);

        when(properties.getMessageQos()).thenReturn(null);
        when(properties.getKeepAliveIntervalSeconds()).thenReturn(null);
        when(properties.getConnectionTimeout()).thenReturn(null);

        return properties;
    }

    private LoProperties getCustomizedLoProperties() {
        when(properties.getApiKey()).thenReturn(API_KEY);
        when(properties.getTopics()).thenReturn(TOPICS);

        when(properties.getHostname()).thenReturn("custom.hostname");
        when(properties.getMqttPersistenceDir()).thenReturn("/tmp");
        when(properties.getMessageQos()).thenReturn(-1);
        when(properties.getKeepAliveIntervalSeconds()).thenReturn(123);
        when(properties.getConnectionTimeout()).thenReturn(321);

        return properties;
    }
}