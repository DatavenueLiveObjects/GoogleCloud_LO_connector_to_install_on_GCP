package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.core.ApiFutureCallback;
import com.orange.lo.sample.lo2pubsub.utils.ConnectorHealthActuatorEndpoint;
import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.fifomqtt.DataManagementFifo;
import com.orange.lo.sdk.mqtt.exceptions.LoMqttException;
import org.springframework.stereotype.Component;

@Component
public class CheckConnectionApiFutureCallbackImpl implements ApiFutureCallback<String> {

    private final DataManagementFifo dataManagementFifo;
    private final ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint;

    public CheckConnectionApiFutureCallbackImpl(LOApiClient loApiClient, ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint) {
        this.dataManagementFifo = loApiClient.getDataManagementFifo();
        this.connectorHealthActuatorEndpoint = connectorHealthActuatorEndpoint;
    }

    @Override
    public void onFailure(Throwable throwable) {
        connectorHealthActuatorEndpoint.setCloudConnectionStatus(false);
    }

    @Override
    public void onSuccess(String s) {
        if (connectorHealthActuatorEndpoint.isCloudConnectionStatus() && connectorHealthActuatorEndpoint.isLoConnectionStatus()) {
            dataManagementFifo.connectAndSubscribe();
        }
    }
}
