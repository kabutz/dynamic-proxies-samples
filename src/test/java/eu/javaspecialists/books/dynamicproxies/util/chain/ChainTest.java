/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
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

package eu.javaspecialists.books.dynamicproxies.util.chain;

import eu.javaspecialists.books.dynamicproxies.util.*;
import org.junit.*;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import static junit.framework.TestCase.*;

public class ChainTest {
  public interface Foo {
    void bar();
    static void baz() {}
    private void bat() {}
    default void bay() {}
  }
  public final class RealFoo {
    public void bar() {}
  }

  @Test
  public void testUnhandledDefaultMethod() {
    VTable vt = VTables.newVTable(RealFoo.class, Foo.class);
    ChainedInvocationHandler chain =
        new VTableHandler(new RealFoo(), vt, null);
    try {
      chain.checkAllMethodsAreHandled(Foo.class);
    } catch (IllegalArgumentException e) {
      assertEquals("Unhandled methods: [public default void eu" +
                       ".javaspecialists.books.dynamicproxies" +
                       ".util.chain.ChainTest$Foo.bay()]",
          e.getMessage());
    }
  }

  @Test
  public void testHandledDefaultMethod() {
    VTable vt = VTables.newVTable(RealFoo.class, Foo.class);
    VTable vt2 = VTables.newDefaultMethodVTable(Foo.class);
    ChainedInvocationHandler chain =
        new VTableHandler(new RealFoo(), vt,
            new VTableDefaultMethodsHandler(vt2, null));
    chain.checkAllMethodsAreHandled(Foo.class);
  }

  @Test
  public void testHandledMultipleInterfaces() {
    VTable vt1 = VTables.newVTable(ArrayDeque.class,
        Deque.class);
    VTable vt2 = VTables.newVTable(ArrayList.class, List.class);
    Object listDeque = Proxy.newProxyInstance(
        List.class.getClassLoader(),
        new Class<?>[] {List.class, Deque.class},
        (proxy, method, args) -> null
    );

    ChainedInvocationHandler chain =
        new VTableHandler(listDeque, vt1,
            new VTableHandler(listDeque, vt2, null));
    chain.checkAllMethodsAreHandled(List.class, Deque.class);
    chain.checkAllMethodsAreHandled(List.class);
    chain.checkAllMethodsAreHandled(Deque.class);
    chain.checkAllMethodsAreHandled(RandomAccess.class);

    try {
      chain.checkAllMethodsAreHandled(List.class,
          SortedSet.class);
      fail("Not all methods from SortedSet are covered");
    } catch (UnhandledMethodException expected) {
    }

    try {
      chain.checkAllMethodsAreHandled(Statement.class);
      fail("Methods from Statement are not covered");
    } catch (UnhandledMethodException expected) {
    }

    try {
      chain.checkAllMethodsAreHandled(ArrayList.class);
      fail("Only interfaces allowed");
    } catch (UnhandledMethodException ex) {
      throw ex;
    } catch (IllegalArgumentException expected) {
    }
  }
}
