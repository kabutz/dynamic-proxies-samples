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

import eu.javaspecialists.books.dynamicproxies.handlers.*;
import org.junit.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import static org.junit.Assert.*;

public class SerializableTest {
  @Test
  public void testVirtualProxy() throws IOException {
    Supplier<List<String>> supplier =
        (Supplier<List<String>> & Serializable) ArrayList::new;
    test(supplier);
    List<String> proxy = Proxies.virtualProxy(
        List.class, supplier);
    test(proxy);
  }

  @Test
  public void testSimpleProxy() throws IOException {
    List<String> proxy = Proxies.simpleProxy(
        List.class, new ArrayList<>());
    test(proxy);
  }

  @Test
  public void testCastProxy() throws IOException {
    InvocationHandler handler =
        (InvocationHandler & Serializable)
            (proxy, method, args) -> null;
    Object proxy = Proxies.castProxy(List.class, handler);
    test(proxy);
  }

  @Test
  public void testSynchronizedProxy() throws IOException {
    Object proxy = Proxies.synchronizedProxy(
        List.class, new ArrayList<>());
    test(proxy);
  }

  @Test
  public void testAdapterProxy() throws IOException {
    Object proxy = Proxies.adapt(List.class, new ArrayList<>(),
        new LinkedList<>());
    try {
      test(proxy);
      fail("Adapters should not be serializable");
    } catch (NotSerializableException success) {
    }
  }

  @Test
  public void testCompositeProxy() throws IOException {
    Object proxy = Proxies.compose(BaseComponent.class);
    try {
      test(proxy);
      fail("Composites should not be serializable");
    } catch (NotSerializableException success) {
    }
  }
  @Test
  public void testFilterProxy() throws IOException {
    Collection<String> proxy = Proxies.filter(
        Collection.class, new ArrayList<>());
    try {
      test(proxy);
      fail("Filters should not be serializable");
    } catch (NotSerializableException success) {
    }
  }

  private void test(Object obj) throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(
        new ByteArrayOutputStream())) {
      out.writeObject(obj);
    }
  }
}
