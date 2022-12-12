package com.orange.lo.sample.lo2pubsub.modify;

import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.orange.lo.sample.lo2pubsub.ConnectorApplication;
import com.orange.lo.sample.lo2pubsub.liveobjects.LoProperties;
import com.orange.lo.sample.lo2pubsub.pubsub.PubSubProperties;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class ModifyConfigurationService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
    private LoProperties loProperties;
	private PubSubProperties pubSubProperties;
	private File configurationFile;
	private File pubSubAuthFile;

    public ModifyConfigurationService(LoProperties loProperties, PubSubProperties pubSubProperties, File configurationFile, File pubSubAuthFile) {
		this.loProperties = loProperties;
		this.pubSubProperties = pubSubProperties;
		this.configurationFile = configurationFile;
		this.pubSubAuthFile = pubSubAuthFile;
    }

    public ModifyConfigurationProperties getProperties() {

    	try {
	        ModifyConfigurationProperties modifyConfigurationProperties = new ModifyConfigurationProperties();
	
	        modifyConfigurationProperties.setLoApiKey(loProperties.getApiKey());
	        modifyConfigurationProperties.setLoTopics(loProperties.getTopics());
	        modifyConfigurationProperties.setPubSubProjectId(pubSubProperties.getProjectId());
			modifyConfigurationProperties.setPubSubAuthFileContent(FileUtils.fileRead(pubSubAuthFile));
	        modifyConfigurationProperties.setPubSubTopicId(pubSubProperties.getTopicId());
	        
	        return modifyConfigurationProperties;
    	} catch (IOException e) {
    		throw new ModifyException("Error while reading configuration", e);
    	}
    }

    public void modify(ModifyConfigurationProperties modifyConfigurationProperties) {
    	try {
    		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    		
			ObjectNode root = (ObjectNode) mapper.readTree(configurationFile);
			
			ObjectNode loNode = (ObjectNode) root.get("lo");
			setProperty(loNode, "api-key", () -> modifyConfigurationProperties.getLoApiKey());
			setProperty(loNode, "topics", () -> modifyConfigurationProperties.getLoTopics());
			
			ObjectNode pubSubNode = (ObjectNode) root.get("google").get("pub-sub");
			setProperty(pubSubNode, "project-id", () -> modifyConfigurationProperties.getPubSubProjectId());
			setProperty(pubSubNode, "topic-id", () -> modifyConfigurationProperties.getPubSubTopicId());

			mapper.writer().writeValue(configurationFile, root);
			
			if (modifyConfigurationProperties.getPubSubAuthFileContent() != null) {
    			FileUtils.fileWrite(pubSubAuthFile, modifyConfigurationProperties.getPubSubAuthFileContent());
    		}
			
    		ConnectorApplication.restart();
		} catch (IOException e) {
			throw new ModifyException("Error while modifying configuration", e);
		}
    }
    private void setProperty(ObjectNode node, String parameterName, Supplier<Object> parameterSupplier) {

		Object parameter = parameterSupplier.get();
		if (Objects.isNull(parameter)) {
			return;
		}

		if (parameter instanceof Integer) {
			node.put(parameterName, (Integer) parameter);
		} else if (parameter instanceof Long) {
			node.put(parameterName, (Long) parameter);
		} else if (parameter instanceof Boolean) {
			node.put(parameterName, (Boolean) parameter);
		} else if (parameter instanceof List && ((List)parameter).size()>0) {
			ArrayNode arrayNode = node.putArray(parameterName);
			((List)parameter).forEach(t-> arrayNode.add((String)t));
		} else if (parameter instanceof String){
			node.put(parameterName, String.valueOf(parameter));
		}
	}
}