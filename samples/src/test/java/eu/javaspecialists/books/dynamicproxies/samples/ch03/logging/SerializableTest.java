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

package eu.javaspecialists.books.dynamicproxies.samples.ch03.logging;

import org.junit.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import static org.junit.Assert.*;

public class SerializableTest {
  @Test
  public void testLoggingProxy() throws IOException {
    Object proxy = Factory.loggingProxy(List.class,
        new ArrayList<>(), Logger.getGlobal());
    try {
      test(proxy);
      fail("Logging proxy should not be serializable");
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
