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
import org.junit.*;

public class FooBarTest {
  private final FooBar fb = Proxies.castProxy(
      FooBar.class, new FooBarInvocationHandler());

  @Test
  public void testFoo() {
    fb.foo();
  }

  @Test(expected = ClassCastException.class)
  public void testBar() {
    fb.bar();
  }

  @Test(expected = ClassCastException.class)
  public void testBarAssign() {
    boolean result = fb.bar();
  }

  @Test(expected = NullPointerException.class)
  public void testBaz() {
    fb.baz();
  }

  @Test(expected = NullPointerException.class)
  public void testBazAssign() {
    int result = fb.baz();
  }
}