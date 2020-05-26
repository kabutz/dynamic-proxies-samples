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

package eu.javaspecialists.books.dynamicproxies.samples.ch02.protection;

import eu.javaspecialists.books.dynamicproxies.samples.ch02.virtual.*;

import java.util.concurrent.atomic.*;
import java.util.stream.*;

import static org.junit.Assert.*;

// tag::listing[]
public class ConcurrentTest {
  public static final int SQUARES = 46_000;
  public void check(CustomMap<Integer, Integer> map) {
    System.out.println(
        "Checking " + map.getClass().getSimpleName());
    try {
      IntStream.range(0, SQUARES)
          .parallel()
          .forEach(i -> map.put(i, i * i));
    } catch (Exception e) {
      System.err.println(e); // carry on with check
    }
    // count actual entries
    var entries = new LongAdder();
    map.forEach((k, v) -> entries.increment());

    System.out.println("entries = " + entries);
    System.out.println("map.size() = " + map.size());

    assertTrue("entries=" + entries + ", " +
                   "map.size=" + map.size(),
        entries.intValue() == map.size());
  }
}
// end::listing[]