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

package eu.javaspecialists.books.dynamicproxies.samples.ch03.virtual;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.samples.ch02.virtual.*;
import org.junit.*;

import java.util.concurrent.atomic.*;

import static org.junit.Assert.*;

public class VirtualProxyTest {
  private static class OurCustomHashMap<K, V> extends CustomHashMap<K, V> {
    private static final AtomicInteger mapsCreated =
        new AtomicInteger();
    {
      mapsCreated.incrementAndGet();
    }
  }

  @Test
  public void testLazyCreation() {
    int numberCreated = OurCustomHashMap.mapsCreated.get();
    CustomMap<Integer, Integer> map =
        Proxies.virtualProxy(CustomMap.class,
            OurCustomHashMap::new);
    assertEquals(numberCreated,
        OurCustomHashMap.mapsCreated.get());
    System.out.println(map.toString());
    assertEquals(numberCreated + 1,
        OurCustomHashMap.mapsCreated.get());
    map.put(1, 1);
    assertEquals(numberCreated + 1,
        OurCustomHashMap.mapsCreated.get());
  }
}
