/**
 * Copyright (c) Orange. All Rights Reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.orange.lo.sample.lo2pubsub.modify;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orange.lo.sample.lo2pubsub.ConnectorApplication;
import com.orange.lo.sample.lo2pubsub.liveobjects.LoProperties;
import com.orange.lo.sample.lo2pubsub.pubsub.PubSubProperties;

@ExtendWith(MockitoExtension.class)
public class ModifyConfigurationServiceTest {

	private static final String LO_API_KEY = "abcd";
	private static final String LO_API_KEY_UPDATED = "dcba";
	
	private static final String PROJECT_ID = "abc11";
	private static final String PROJECT_ID_UPDATED = "abc22";
	
	private static final String AUTH = "auth11";
	private static final String AUTH_UPDATED = "auth12";
	
	@TempDir
	File tempDir;
	
	private File configurationFile;
	private File pubSubAuthFile;
	
	private ModifyConfigurationService modifyConfigurationService;
	
	@BeforeEach
    void setUp() throws IOException {
		LoProperties loProperties = new LoProperties();
		loProperties.setApiKey(LO_API_KEY);
		
		PubSubProperties pubSubProperties = new PubSubProperties();
		pubSubProperties.setProjectId(PROJECT_ID);
		
		
		configurationFile = new File(tempDir, "application.yml");
		FileUtils.fileWrite(configurationFile, "lo:\n  api-key: " + LO_API_KEY + "\ngoogle:\n  pub-sub:\n    project-id: " + PROJECT_ID);

		pubSubAuthFile = new File(tempDir, "auth.json");
		FileUtils.fileWrite(pubSubAuthFile, AUTH);
		
		modifyConfigurationService = new ModifyConfigurationService(loProperties, pubSubProperties, configurationFile, pubSubAuthFile);
	}
	
	@Test
	public void shouldReadParameters() {
		//when
		ModifyConfigurationProperties properties = modifyConfigurationService.getProperties();
		
		//then
		Assertions.assertEquals(LO_API_KEY, properties.getLoApiKey());
		Assertions.assertEquals(PROJECT_ID, properties.getPubSubProjectId());
		Assertions.assertEquals(AUTH, properties.getPubSubAuthFileContent());
	}
	
	@Test
	public void shouldUpdateParameters() throws IOException {
		//given
		MockedStatic<ConnectorApplication> connectorApplication = Mockito.mockStatic(ConnectorApplication.class);
		ModifyConfigurationProperties modifyConfigurationProperties = new ModifyConfigurationProperties();
		modifyConfigurationProperties.setPubSubProjectId(PROJECT_ID_UPDATED);
		modifyConfigurationProperties.setLoApiKey(LO_API_KEY_UPDATED);
		modifyConfigurationProperties.setPubSubAuthFileContent(AUTH_UPDATED);
		
		//when
		modifyConfigurationService.modify(modifyConfigurationProperties);
		
		//then
		String configuratioFileContent = FileUtils.fileRead(configurationFile);
		
		connectorApplication.verify(ConnectorApplication::restart);
		Assertions.assertTrue(configuratioFileContent.contains(LO_API_KEY_UPDATED));
		Assertions.assertTrue(configuratioFileContent.contains(PROJECT_ID_UPDATED));
		
		String pubSubAuthFileContent = FileUtils.fileRead(pubSubAuthFile);
		Assertions.assertTrue(pubSubAuthFileContent.contains(AUTH_UPDATED));
	}
}