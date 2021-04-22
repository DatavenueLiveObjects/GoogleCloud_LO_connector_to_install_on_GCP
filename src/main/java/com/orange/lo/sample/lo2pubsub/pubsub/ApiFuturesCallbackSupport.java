/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.pubsub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.stereotype.Component;

@Component
public class ApiFuturesCallbackSupport {

    public void addCallback(ApiFuture<String> future, final ApiFutureCallback<String> callback) {
        ApiFutures.addCallback(future, callback, MoreExecutors.directExecutor());
    }

}
