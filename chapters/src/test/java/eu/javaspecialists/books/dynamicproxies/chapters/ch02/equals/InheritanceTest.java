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

package eu.javaspecialists.books.dynamicproxies.chapters.ch02.equals;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.chapters.ch02.virtual.*;
import org.junit.*;

import static org.junit.Assert.*;

public class InheritanceTest {
  @Test
  public void testFixedCustomMapEqualsWithInheritance() {
    CustomMap<Integer, Integer> map1 =
        new FixedEqualsInCustomHashMap.CustomHashMap<>();
    CustomMap<Integer, Integer> map2 =
        new ExtendedCustomHashMap<>();
    assertEquals(map1, map2);
    assertEquals(map2, map1);
    map1.put(1, 2);
    assertNotEquals(map1, map2);
    assertNotEquals(map2, map1);
    map2.put(1, 2);
    assertEquals(map1, map2);
    assertEquals(map2, map1);
  }
  @Test
  public void testFixedCustomMapEqualsWithHandcraftedProxies() {
    CustomMap<Integer, Integer> map1 =
        new FixedEqualsInCustomHashMap.CustomHashMap<>();
    CustomMap<Integer, Integer> map2 =
        new ExtendedCustomHashMap<>();
    CustomMap<Integer, Integer> proxy1 = 
        new FixedEqualsInHandCraftedProxy.UnmodifiableCustomMap<>(map1);
    CustomMap<Integer, Integer> proxy2 = 
        new FixedEqualsInHandCraftedProxy.UnmodifiableCustomMap<>(map2);
      assertEquals(proxy1, proxy2);
      assertEquals(proxy2, proxy1);
      map1.put(1, 2);
      assertNotEquals(proxy1, proxy2);
      assertNotEquals(proxy2, proxy1);
      map2.put(1, 2);
      assertEquals(proxy1, proxy2);
      assertEquals(proxy2, proxy1);
  }
  @Test
  public void testFixedCustomMapEqualsWithDynamicProxies() {
    CustomMap<Integer, Integer> map1 =
        new FixedEqualsInCustomHashMap.CustomHashMap<>();
    CustomMap<Integer, Integer> map2 =
        new ExtendedCustomHashMap<>();
    CustomMap<Integer, Integer> proxy1 =
        Proxies.simpleProxy(CustomMap.class, map1);
    CustomMap<Integer, Integer> proxy2 =
        Proxies.simpleProxy(CustomMap.class, map2);
      assertEquals(proxy1, proxy2);
      assertEquals(proxy2, proxy1);
      map1.put(1, 2);
      assertNotEquals(proxy1, proxy2);
      assertNotEquals(proxy2, proxy1);
      map2.put(1, 2);
      assertEquals(proxy1, proxy2);
      assertEquals(proxy2, proxy1);
  }

  private static class ExtendedCustomHashMap<K, V> extends
      FixedEqualsInCustomHashMap.CustomHashMap<K, V> {
  }
}
