/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bytedance.bitsail.connector.druid.sink;

import com.bytedance.bitsail.common.configuration.BitSailConfiguration;
import com.bytedance.bitsail.connector.druid.option.DruidWriterOptions;
import com.bytedance.bitsail.test.connector.test.EmbeddedFlinkCluster;
import com.bytedance.bitsail.test.connector.test.utils.JobConfUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

public class DruidSinkITCase {
  private static final String DRUID_SERVICE_NAME = "router";
  private static final int DRUID_SERVICE_PORT = 8888;
  private DockerComposeContainer environment;

  @Before
  public void setup() {
    environment = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService(
                    DRUID_SERVICE_NAME,
                    DRUID_SERVICE_PORT,
                    Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(180))
            );
    environment.start();
  }

  @Test
  public void testBatchJob() throws Exception {

    // Arrange
    final String coordinatorURL = environment.getServiceHost(DRUID_SERVICE_NAME, DRUID_SERVICE_PORT) + ":" +
            environment.getServicePort(DRUID_SERVICE_NAME, DRUID_SERVICE_PORT);
    final BitSailConfiguration jobConfiguration = JobConfUtils.fromClasspath("fake_to_druid.json");
    jobConfiguration.set(DruidWriterOptions.COORDINATOR_URL, coordinatorURL);

    // Act; Assert
    EmbeddedFlinkCluster.submitJob(jobConfiguration);
  }

  @After
  public void after() {
    environment.close();
  }
}
