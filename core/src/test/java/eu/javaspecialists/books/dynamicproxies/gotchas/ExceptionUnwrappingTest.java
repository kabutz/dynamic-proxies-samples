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

package eu.javaspecialists.books.dynamicproxies.gotchas;

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
    var eh = (ExceptionUnwrappingInvocationHandler) h;
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
  public void testSynchronizedProxy() {
    try {
      Collection<String> test = Proxies.synchronizedProxy(
          Collection.class, List.of());
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

  @Test
  public void testAdapterProxy() {
    testAdapter(createStandard(), true);
    testAdapter(createWithoutProxiesCast(), false);
    testAdapter(createWithoutProxiesWrapped(), true);
  }

  private void testAdapter(List<String> greeting,
                           boolean correctExceptionExpected) {
    assertEquals(2, greeting.size());
    checkExceptions(() -> greeting.add("Hello world"), correctExceptionExpected);
    greeting.remove(1);
    assertEquals(1, greeting.size());
  }

  private static List<String> createStandard() {
    return Proxies.adapt(
        List.class, new ArrayList<>(
            List.of("Hello", "World")), new HideAdd());
  }

  @SuppressWarnings("unchecked")
  private static List<String> createWithoutProxiesCast() {
    return (List<String>) Proxy.newProxyInstance(
        List.class.getClassLoader(),
        new Class<?>[] {List.class},
        createAdapterHandler());
  }

  @SuppressWarnings("unchecked")
  private static List<String> createWithoutProxiesWrapped() {
    return (List<String>) Proxy.newProxyInstance(
        List.class.getClassLoader(),
        new Class<?>[] {List.class},
        new ExceptionUnwrappingInvocationHandler(
            createAdapterHandler()));
  }

  private static ObjectAdapterHandler createAdapterHandler() {
    return new ObjectAdapterHandler(
        List.class, new ArrayList<>(
        List.of("Hello", "World")), new HideAdd());
  }

  public static class HideAdd {
    public boolean add(Object o) {
      throw new UnsupportedOperationException("no add");
    }
  }

  private void checkExceptions(Runnable job,
                               boolean correctExceptionExpected) {
    try {
      job.run();
      fail("Expected an UnsupportedOperationException");
    } catch (UndeclaredThrowableException ex) {
      if (correctExceptionExpected) {
        fail("Expected to see an UnsupportedOperationException");
      }
    } catch (UnsupportedOperationException ex) {
      if (!correctExceptionExpected)
        fail("Expected to see an UndeclaredThrowableException");
    }
  }

  public interface Contact extends BaseComponent<Contact> {
    void sendMail(String body);
    int count();
  }

  public class Person implements Contact {
    private final String email;
    public Person(String email) {this.email = email;}
    @Override
    public void sendMail(String body) {
      // Implementation not needed for the test
    }
    @Override
    public int count() {
      return 1;
    }
  }

  @Test
  public void testComposite() {
    Contact tjsn = Proxies.compose(Contact.class);
    Contact students = Proxies.compose(Contact.class);
    tjsn.add(students);
    students.add(new Person("john@aol.com"));
    students.add(new Person("peter@aol.com"));
    tjsn.add(new Person("heinz@javaspecialists.eu"));

    tjsn.sendMail("Hello from TJSN");

    students.add(new Incognito());

    try {
      tjsn.sendMail("hello again from TJSN");
      fail("Expected to see an IllegalStateException");
    } catch (IllegalStateException success) {
    }
  }

  public static class Incognito implements Contact {
    @Override
    public void sendMail(String body) {
      throw new IllegalStateException("anon");
    }
    @Override
    public int count() {
      return 1;
    }
  }
}
