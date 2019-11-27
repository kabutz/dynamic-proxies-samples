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

package eu.javaspecialists.books.dynamicproxies.ch02.equals;

import eu.javaspecialists.books.dynamicproxies.ch02.equals.FixedEqualsInCustomHashMap.*;
import eu.javaspecialists.books.dynamicproxies.ch02.equals.FixedEqualsInHandCraftedProxy.*;
import org.junit.*;

import static org.junit.Assert.*;

// tag::listing[]
public class CustomMapEqualsTest {
  @Test
  public void testEqualsContract() {
    var real = new CustomHashMap<Integer, Integer>();
    for (int i = 0; i < 10; i++) real.put(i, i * i);
    var proxy1 = new UnmodifiableCustomMap<>(real);
    var proxy2 = new UnmodifiableCustomMap<>(real);
    var empty = new CustomHashMap<Integer, Integer>();

    // reflexive
    assertEquals(real, real);
    assertEquals(proxy1, proxy1);
    assertEquals(proxy2, proxy2);

    // symmetric
    assertEquals(real, proxy1);
    assertEquals(proxy1, real);
    assertEquals(proxy1, proxy2);
    assertEquals(proxy2, proxy1);

    // transitive
    if (real.equals(empty) && empty.equals(proxy2))
      assertEquals(real, proxy2);
    if (real.equals(proxy1) && proxy1.equals(proxy2))
      assertEquals(real, proxy2);
  }
}
// end::listing[]