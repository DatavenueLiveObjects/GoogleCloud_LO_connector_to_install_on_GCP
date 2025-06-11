/**
 * Copyright (c) Orange, Inc. and its affiliates. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.core.ApiFutureCallback;
import com.orange.lo.sample.lo2pubsub.utils.Counters;
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
    private final Counters counters;

    public CheckConnectionApiFutureCallbackImpl(LOApiClient loApiClient, Counters counters) {
        this.dataManagementFifo = loApiClient.getDataManagementFifo();
        this.counters = counters;
    }

    @Override
    public void onFailure(Throwable throwable) {
        LOG.error("Problem with connection. Check GCP credentials. {}", throwable.getLocalizedMessage());
        counters.setCloudConnectionStatus(false);
        dataManagementFifo.disconnect();

    }

    @Override
    public void onSuccess(String s) {
        counters.setCloudConnectionStatus(true);
        dataManagementFifo.disconnect();
    }
}
