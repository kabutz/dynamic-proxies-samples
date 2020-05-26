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

package eu.javaspecialists.books.dynamicproxies.chapters.ch05.bettercollection;

import java.util.*;

// To include default methods of HashSet's interfaces, use
// --add-opens java.base/java.util=eu.javaspecialists.books
// .dynamicproxies
// --add-opens java.base/java.lang=eu.javaspecialists.books
// .dynamicproxies
@SuppressWarnings("unchecked")
public class BetterCollectionDynamicObjectAdapterDemo {
  public static void main(String... args) {
    // tag::listing[]
    BetterCollection<String> names =
        BetterCollectionFactory.asBetterCollection(
            new HashSet<>(), new String[0]);
    names.add("Wolfgang");
    names.add("Bobby Tables");
    names.add("Leander");
    names.add("Klaus");
    names.add("Menongahela");
    String[] nameArray = names.toArray();
    for (String name : nameArray) {
      System.out.println(name.toUpperCase());
    }
    System.out.println(names);

    ((Collection) names).add(42); // this will fail
    // end::listing[]
  }
}
