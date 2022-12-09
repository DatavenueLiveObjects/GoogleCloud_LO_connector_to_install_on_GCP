package com.orange.lo.sample.lo2pubsub.modify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ModifyConfigurationService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ModifyConfigurationService() {
    }

    public ModifyConfigurationProperties getProperties() {
        ModifyConfigurationProperties modifyConfigurationProperties = new ModifyConfigurationProperties();

        LOG.info(modifyConfigurationProperties.toString());

        return modifyConfigurationProperties;
    }

    public void modify(ModifyConfigurationProperties modifyConfigurationProperties) {
        LOG.info(modifyConfigurationProperties.toString());
    }
}