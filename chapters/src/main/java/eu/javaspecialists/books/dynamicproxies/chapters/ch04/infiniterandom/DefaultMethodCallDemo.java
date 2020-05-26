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

package eu.javaspecialists.books.dynamicproxies.chapters.ch04.infiniterandom;

import eu.javaspecialists.books.dynamicproxies.*;

import java.util.*;

// tag::listing[]
/**
 * If we are inside a module, we need to explicitly open the
 * java.util package inside the java.base module to our module.
 * We can do this with the JVM parameters --add-opens \
 * java.base/java.util=eu.javaspecialists.books.dynamicproxies
 */
public class DefaultMethodCallDemo {
  public static void main(String... args) {
    Iterator<Double> filter = Proxies.filter(
        Iterator.class, new InfiniteRandomDouble());

    System.out.println(filter.hasNext());
    System.out.println(filter.next());

    try {
      filter.remove();
      throw new AssertionError(
          "Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      System.err.println(expected);
    }

    System.out.println("Iterating forever ...");

    filter.forEachRemaining(System.out::println);
  }
}
// end::listing[]