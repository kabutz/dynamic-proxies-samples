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
}
