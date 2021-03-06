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
package org.jclouds.ssh;

import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.io.Payload;
import org.jclouds.net.IPSocket;

/**
 * @author Adrian Cole
 */
public interface SshClient {

   interface Factory {

      /**
       * To be removed in jclouds 1.5.0
       * 
       * @see #create(IPSocket, LoginCredentials)
       */
      @Deprecated
      SshClient create(IPSocket socket, Credentials credentials);
      
      SshClient create(IPSocket socket, LoginCredentials credentials);

   }

   String getUsername();

   String getHostAddress();

   void put(String path, Payload contents);

   Payload get(String path);

   ExecResponse exec(String command);

   void connect();

   void disconnect();

   void put(String path, String contents);

}