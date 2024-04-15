package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.core.ApiFutureCallback;
import com.orange.lo.sample.lo2pubsub.utils.ConnectorHealthActuatorEndpoint;
import com.orange.lo.sdk.LOApiClient;
import com.orange.lo.sdk.fifomqtt.DataManagementFifo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class CheckConnectionApiFutureCallbackImpl implements ApiFutureCallback<String> {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DataManagementFifo dataManagementFifo;
    private final ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint;

    public CheckConnectionApiFutureCallbackImpl(LOApiClient loApiClient, ConnectorHealthActuatorEndpoint connectorHealthActuatorEndpoint) {
        this.dataManagementFifo = loApiClient.getDataManagementFifo();
        this.connectorHealthActuatorEndpoint = connectorHealthActuatorEndpoint;
    }

    @Override
    public void onFailure(Throwable throwable) {
        LOG.error("Problem with connection. Check GCP credentials. ", throwable.getLocalizedMessage());
        connectorHealthActuatorEndpoint.setCloudConnectionStatus(false);
        dataManagementFifo.disconnect();

    }

    @Override
    public void onSuccess(String s) {
        connectorHealthActuatorEndpoint.setCloudConnectionStatus(true);
        dataManagementFifo.disconnect();
    }
}
