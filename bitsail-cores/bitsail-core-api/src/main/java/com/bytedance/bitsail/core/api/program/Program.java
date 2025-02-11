/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.bytedance.bitsail.core.api.program;

import com.bytedance.bitsail.base.execution.ExecutionEnviron;
import com.bytedance.bitsail.base.extension.Component;
import com.bytedance.bitsail.base.packages.PluginFinder;
import com.bytedance.bitsail.common.configuration.BitSailConfiguration;
import com.bytedance.bitsail.core.api.command.CoreCommandArgs;
import com.bytedance.bitsail.core.api.program.factory.ProgramDAGBuilderFactory;

import java.io.Serializable;

public interface Program extends Serializable, Component {

  /**
   * Construct program.
   */
  void configure(PluginFinder pluginFinder,
                 BitSailConfiguration globalConfiguration,
                 CoreCommandArgs coreCommandArgs) throws Exception;

  /**
   * Validate the parameters for the program.
   */
  default boolean validate() throws Exception {
    return true;
  }

  /**
   * Submit program.
   */
  void submit() throws Exception;

  /**
   * Create program dag builder factory
   */
  ProgramDAGBuilderFactory createProgramBuilderFactory();

  /**
   * Create execution for the program.
   */
  ExecutionEnviron createExecutionEnviron();
}
