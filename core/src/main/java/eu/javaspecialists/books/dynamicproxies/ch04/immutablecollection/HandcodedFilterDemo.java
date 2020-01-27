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

package eu.javaspecialists.books.dynamicproxies.ch04.immutablecollection;

import java.util.*;

public class HandcodedFilterDemo {
  public static void main(String... args) {
    // tag::listing[]
    ImmutableCollection<String> names =
        new HandcodedFilter<>(
            Arrays.asList("Peter", "Paul", "Mary"));
    // names.remove("Peter"); // does not compile
    System.out.println(names);
    System.out.println("Is Mary in? " + names.contains("Mary"));
    System.out.println("Are there names? " + !names.isEmpty());
    System.out.println("Printing the names:");
    names.forEach(System.out::println);
    System.out.println("Class: " +
                           names.getClass().getSimpleName());

    names.printAll();
    // end::listing[]
  }
}

