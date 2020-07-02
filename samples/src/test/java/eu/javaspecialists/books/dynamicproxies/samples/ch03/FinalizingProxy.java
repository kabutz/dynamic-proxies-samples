/*
 * Copyright (C) 2020 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz
 * licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */

package eu.javaspecialists.books.dynamicproxies.samples.ch03;

import eu.javaspecialists.books.dynamicproxies.util.*;
import org.junit.*;

import java.lang.reflect.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class FinalizingProxy {
  public interface Finalizable {
    void finalize();
  }

  @Test
  public void finalizeCalled() throws Exception {
    MethodKey finalizeMethod = new MethodKey(
        Object.class.getDeclaredMethod("finalize"));
    CountDownLatch latch = new CountDownLatch(1);
    Runnable job = (Runnable) Proxy.newProxyInstance(
        FinalizingProxy.class.getClassLoader(),
        new Class<?>[] {Runnable.class, Finalizable.class},
        (proxy, method, arguments) -> {
          System.out.println("method = " + method);
          if (finalizeMethod.matches(method)) latch.countDown();
          return null;
        }
    );
    job.run();
    job = null;
    for (int i = 0; i < 3; i++) {
      System.gc();
      System.runFinalization();
    }

    assertTrue("finalize() not called in time",
        latch.await(100, TimeUnit.MILLISECONDS));
  }
}
