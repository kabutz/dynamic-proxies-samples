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

package eu.javaspecialists.books.dynamicproxies;

import org.junit.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import static org.junit.Assert.*;

public class ProxiesCastTest {
  private static final InvocationHandler HANDLER =
      (proxy, method, args) -> null;
  @Test
  public void testCastParametersNull() {
    checkFor(NullPointerException.class,
        () -> Proxies.castProxy(null, HANDLER));
    checkFor(NullPointerException.class,
        () -> Proxies.castProxy(Iterable.class, null));
    checkFor(NullPointerException.class,
        () -> Proxies.castProxy(Iterable.class, HANDLER,
            (Class<?>[]) null));
  }
  @Test
  public void testCastParametersNotInterfaces() {
    checkFor(IllegalArgumentException.class,
        () -> Proxies.castProxy(Object.class, HANDLER));

    checkFor(IllegalArgumentException.class,
        () -> Proxies.castProxy(Iterable.class, HANDLER,
            Object.class));
  }

  @Test
  public void testCastUsesAllInterfaces() {
    List<String> list = Proxies.castProxy(List.class, HANDLER,
        Queue.class, Iterable.class, Serializable.class,
        RandomAccess.class, Comparator.class);
    assertTrue(list instanceof List);
    assertTrue(list instanceof Queue);
    assertTrue(list instanceof Iterable);
    assertTrue(list instanceof Serializable);
    assertTrue(list instanceof RandomAccess);
    assertTrue(list instanceof Comparator);
  }

  private void checkFor(Class<? extends Throwable> expected,
                        Runnable task) {
    try {
      task.run();
      fail("Expected " + expected.getSimpleName());
    } catch (Throwable ex) {
      if (!expected.isInstance(ex)) {
        fail("Expected " + expected.getSimpleName() +
                 " but got " + ex.getClass().getSimpleName());
      }
    }
  }
}
