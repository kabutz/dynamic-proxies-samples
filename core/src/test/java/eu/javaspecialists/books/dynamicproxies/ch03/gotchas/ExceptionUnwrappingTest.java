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

package eu.javaspecialists.books.dynamicproxies.ch03.gotchas;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.handlers.*;
import org.junit.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class ExceptionUnwrappingTest {
  @Test
  public void testInvocationHandler() {
    InvocationHandler rootHandler =
        (proxy, method, args) -> null;
    Collection<String> test = Proxies.castProxy(
        Collection.class, rootHandler);
    InvocationHandler h = Proxy.getInvocationHandler(test);
    assertTrue(
        "expected InvocationHandler to be exception unwrapping",
        h instanceof ExceptionUnwrappingInvocationHandler);
    var eh = (ExceptionUnwrappingInvocationHandler)h;
    assertSame(rootHandler, eh.getNestedInvocationHandler());
  }

  @Test
  public void testRuntimeExceptions() {
    try {
      Collection<String> test = Proxies.simpleProxy(
          Collection.class, List.of());
      test.clear();
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException success) {
    }
  }

  @Test
  public void testIOException() {
    try {
      FileReaderWizard wiz = Proxies.simpleProxy(
          FileReaderWizard.class, () -> {
            throw new IOException("File has magically vanished");
          }
      );
      wiz.open();
      fail("Expected an IOException");
    } catch (IOException success) {
    }
  }

  public interface FileReaderWizard {
    void open() throws IOException;
  }

  @Test
  public void testFalsePositivesException() {
    try {
      MethodCallerWizard wiz = Proxies.simpleProxy(
          MethodCallerWizard.class, () -> {
            throw new InvocationTargetException(
                new TimeoutException(
                    "time flies like an arrow"));
          }
      );
      wiz.call();
      fail("Expected an InvocationTargetException");
    } catch (InvocationTargetException success) {
      try {
        throw success.getCause();
      } catch (TimeoutException superSuccess) {
        assertEquals("time flies like an arrow",
            superSuccess.getMessage());
      } catch (Throwable throwable) {
        fail("Unexpected throwable hidden inside: " + throwable);
      }
    }
  }

  public interface MethodCallerWizard {
    void call() throws InvocationTargetException;
  }

  @Test
  public void testVirtualProxy() {
    try {
      Collection<String> test = Proxies.virtualProxy(
          Collection.class, List::of);
      test.add("Hello world");
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException success) {
    }
  }

  @Test
  public void testFilterProxy() {
    try {
      Collection<String> test = Proxies.filter(
          Collection.class, List.of());
      test.add("Hello world");
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException success) {
    }
  }
  // TODO: Add tests for proxies, adapter, composite, etc.
}
