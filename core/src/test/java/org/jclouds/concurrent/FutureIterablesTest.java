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
package org.jclouds.concurrent;

import static org.jclouds.concurrent.FutureIterables.transformParallel;
import static org.testng.Assert.assertEquals;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.jclouds.logging.Logger;
import org.jclouds.rest.AuthorizationException;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

/**
 * Tests behavior of FutureIterables
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", singleThreaded = true, testName = "FutureIterablesTest")
public class FutureIterablesTest {

   public void testAuthorizationExceptionPropagatesAndOnlyTriesOncePerElement() {
      final AtomicInteger counter = new AtomicInteger();

      try {
         transformParallel(ImmutableSet.of("hello", "goodbye"), new Function<String, Future<String>>() {

            @Override
            public Future<String> apply(String input) {
               counter.incrementAndGet();
               return com.google.common.util.concurrent.Futures.immediateFailedFuture(new AuthorizationException());
            }

         }, MoreExecutors.sameThreadExecutor(), null, Logger.CONSOLE, "");
         assert false;
      } catch (AuthorizationException e) {
         assertEquals(counter.get(), 2);
      }

   }
   
   public void testNormalExceptionPropagatesAsTransformParallelExceptionAndTries5XPerElement() {
      final AtomicInteger counter = new AtomicInteger();

      try {
         transformParallel(ImmutableSet.of("hello", "goodbye"), new Function<String, Future<String>>() {

            @Override
            public Future<String> apply(String input) {
               counter.incrementAndGet();
               return com.google.common.util.concurrent.Futures.immediateFailedFuture(new RuntimeException());
            }

         }, MoreExecutors.sameThreadExecutor(), null, Logger.CONSOLE, "");
         assert false;
      } catch (TransformParallelException e) {
         assertEquals(e.getFromToException().size(), 2);
         assertEquals(counter.get(), 10);
      }

   }

}
