/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
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

package org.jclouds.virtualbox.functions.admin;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jclouds.compute.options.RunScriptOptions.Builder.runAsRoot;

import java.net.URI;

import org.jclouds.compute.callables.RunScriptOnNode;
import org.jclouds.compute.callables.RunScriptOnNode.Factory;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.domain.NodeState;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.predicates.RetryIfSocketNotYetOpen;
import org.jclouds.net.IPSocket;
import org.jclouds.scriptbuilder.domain.Statements;
import org.testng.annotations.Test;
import org.virtualbox_4_1.VirtualBoxManager;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Suppliers;

@Test(groups = "unit", singleThreaded = true, testName = "StartVBoxIfNotAlreadyRunningTest")
public class StartVBoxIfNotAlreadyRunningTest {

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Test
   public void testStartVboxConnectsToManagerWhenPortAlreadyListening() throws Exception {
      VirtualBoxManager manager = createMock(VirtualBoxManager.class);
      Factory runScriptOnNodeFactory = createMock(Factory.class);
      RetryIfSocketNotYetOpen client = createMock(RetryIfSocketNotYetOpen.class);
      NodeMetadata host = new NodeMetadataBuilder().id("host").state(NodeState.RUNNING).build();
      URI provider = URI.create("http://localhost:18083/");
      String identity = "adminstrator";
      String credential = "12345";

      expect(client.apply(new IPSocket(provider.getHost(), provider.getPort()))).andReturn(true);

      manager.connect(provider.toASCIIString(), identity, credential);

      replay(manager, runScriptOnNodeFactory, client);

      new StartVBoxIfNotAlreadyRunning((Function) Functions.constant(manager), runScriptOnNodeFactory, client,
               Suppliers.ofInstance(host), Suppliers.ofInstance(provider), identity, credential).start();

      verify(manager, runScriptOnNodeFactory, client);

   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Test
   public void testStartVboxDisablesPasswordAccessOnWebsrvauthlibraryStartsVboxwebsrvInBackgroundAndConnectsManagerWhenPortIsNotListening()
            throws Exception {
      VirtualBoxManager manager = createMock(VirtualBoxManager.class);
      Factory runScriptOnNodeFactory = createMock(Factory.class);
      RetryIfSocketNotYetOpen client = createMock(RetryIfSocketNotYetOpen.class);
      RunScriptOnNode runScriptOnNode = createMock(RunScriptOnNode.class);
      NodeMetadata host = new NodeMetadataBuilder().id("host").state(NodeState.RUNNING).operatingSystem(
               OperatingSystem.builder().description("unix").build()).build();
      URI provider = URI.create("http://localhost:18083/");
      String identity = "adminstrator";
      String credential = "12345";

      expect(client.apply(new IPSocket(provider.getHost(), provider.getPort()))).andReturn(false);
      expect(
               runScriptOnNodeFactory.create(host, Statements.exec("VBoxManage setproperty websrvauthlibrary null"),
                        runAsRoot(false).wrapInInitScript(false))).andReturn(runScriptOnNode);
      expect(runScriptOnNode.init()).andReturn(runScriptOnNode);
      expect(runScriptOnNode.call()).andReturn(new ExecResponse("", "", 0));
      
      expect(
               runScriptOnNodeFactory.create(host, Statements.exec("vboxwebsrv -t 10000 -v -b"), runAsRoot(false)
                        .wrapInInitScript(false).blockOnComplete(false).nameTask("vboxwebsrv"))).andReturn(
               runScriptOnNode);
      expect(runScriptOnNode.init()).andReturn(runScriptOnNode);
      expect(runScriptOnNode.call()).andReturn(new ExecResponse("", "", 0));
      
      manager.connect(provider.toASCIIString(), identity, credential);

      replay(manager, runScriptOnNodeFactory, runScriptOnNode, client);
      new StartVBoxIfNotAlreadyRunning((Function) Functions.constant(manager), runScriptOnNodeFactory, client,
               Suppliers.ofInstance(host), Suppliers.ofInstance(provider), identity, credential).start();
      verify(manager, runScriptOnNodeFactory, runScriptOnNode, client);

   }
}