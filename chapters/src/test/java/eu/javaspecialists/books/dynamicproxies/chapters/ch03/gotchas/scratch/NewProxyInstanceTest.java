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

package eu.javaspecialists.books.dynamicproxies.chapters.ch03.gotchas.scratch;

import eu.javaspecialists.books.dynamicproxies.*;
import org.junit.*;

import java.lang.reflect.*;

import static org.junit.Assert.*;

public class NewProxyInstanceTest {
  private final static InvocationHandler UNSUPPORTED =
      (proxy, method, args) -> {
        throw new UnsupportedOperationException("not implemented");
      };

  @Test(expected = IllegalArgumentException.class)
  public void classIsString() {
    Proxies.castProxy(String.class, UNSUPPORTED);
  }
  @Test(expected = IllegalArgumentException.class)
  public void classIsPrimitive() {
    Proxies.castProxy(int.class, UNSUPPORTED);
  }
  @Test(expected = IllegalArgumentException.class)
  public void classIsAbstractNumber() {
    Proxies.castProxy(Number.class, UNSUPPORTED);
  }
  @Test(expected = IllegalArgumentException.class)
  public void duplicateClasses() {
    Proxy.newProxyInstance(
        Runnable.class.getClassLoader(),
        new Class<?>[] {Runnable.class, Runnable.class},
        UNSUPPORTED
    );
  }

  @Test
  public void packageAccessInterface() throws ClassNotFoundException {
    Class<?> clazz = Class.forName(
        getClass().getPackageName() + ".hidden.Foo");
    Object proxy = Proxy.newProxyInstance(
        clazz.getClassLoader(),
        new Class<?>[] {clazz},
        UNSUPPORTED
    );
    assertEquals(clazz.getPackageName(),
        proxy.getClass().getPackageName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void twoPackageAccessInterfaces() throws ClassNotFoundException {
    Class<?> clazz = Class.forName(
        getClass().getPackageName() + ".hidden.Foo");
    Proxy.newProxyInstance(
        clazz.getClassLoader(),
        new Class<?>[] {Foo.class, clazz},
        UNSUPPORTED
    );
  }

  @Test
  public void packageName() throws ClassNotFoundException {
    Object proxy = Proxy.newProxyInstance(
        Runnable.class.getClassLoader(),
        new Class<?>[] {Runnable.class},
        UNSUPPORTED
    );
    System.out.println(proxy.getClass());
    System.out.println(proxy.getClass().getPackageName());
    System.out.println(proxy.getClass().getClassLoader());
  }

  @Test(expected = IllegalArgumentException.class)
  public void invisibleInterface() throws ClassNotFoundException {
    Object proxy = Proxy.newProxyInstance(
        Runnable.class.getClassLoader(),
        new Class<?>[] {Foo.class},
        UNSUPPORTED
    );
    System.out.println(proxy.getClass());
    System.out.println(proxy.getClass().getPackageName());
  }

  @Test
  public void visibleInterface() throws ClassNotFoundException {
    Object proxy = Proxy.newProxyInstance(
        Foo.class.getClassLoader(),
        new Class<?>[] {Runnable.class},
        UNSUPPORTED
    );
    System.out.println(proxy.getClass());
    System.out.println(proxy.getClass().getPackageName());
  }

  @Test
  public void testDifferentClassLoaders3() throws ClassNotFoundException {
    System.out.println(ClassLoader.getPlatformClassLoader());
    System.out.println(ClassLoader.getSystemClassLoader());
    Object proxy = Proxy.newProxyInstance(
        Foo.class.getClassLoader(),
        new Class<?>[] {Foo.class},
        UNSUPPORTED
    );
    System.out.println(proxy.getClass());
    System.out.println(proxy.getClass().getPackageName());
    System.out.println(proxy.getClass().getClassLoader());
    System.out.println(Runnable.class.getClassLoader());
  }

  @Test
  public void packagePrivatePackageBootClassPath()
      throws ClassNotFoundException {
    // package private class
    Class<?> intf = Class.forName("java.util.stream.Sink");
    InvocationHandler h = new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method,
                           Object[] args) throws Throwable {
        System.out.println("Hackity.invoke");
        return null;
      }
    };
    Object proxy = Proxies.castProxy(intf, h);

    String intfPackageName = intf.getPackageName();
    String proxyPackageName = proxy.getClass().getPackageName();
    assertEquals("java.util.stream", intfPackageName);
    assertEquals("java.util.stream", proxyPackageName);
    assertNull(proxy.getClass().getClassLoader());
  }
}