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

package eu.javaspecialists.books.dynamicproxies.samples.ch04;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.samples.ch02.virtual.*;
import org.junit.*;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class CustomHashMapCreation {
  @Test
  public void testFilterCustomMap() {
    CustomMap<Integer, Integer> map1 = new CustomHashMap<>();
    CustomMap<Integer, Integer> map2 =
        Proxies.filter(CustomMap.class, new HashMap<>());

    CustomMap<Integer, Integer> map3 =
        Proxies.virtualProxy(
            CustomMap.class,
            () -> Proxies.filter(CustomMap.class,
                new HashMap<>())
        );

    CustomMap<Integer, Integer> map4 =
        Proxies.synchronizedProxy(CustomMap.class,
            Proxies.virtualProxy(CustomMap.class,
                () -> Proxies.filter(CustomMap.class,
                    new HashMap<>())));

    for (int i = 0; i < 46_000; i++) {
      map1.put(i, i * i);
      map2.put(i, i * i);
    }

    ThreadLocalRandom.current().ints(1000, 0, 46_000)
        .forEach(i -> {
          map1.remove(i);
          map2.remove(i);
        });

    String s1 = map1.toString();
    String s2 = map2.toString();

    assertEquals(s1, s2);
  }
}
