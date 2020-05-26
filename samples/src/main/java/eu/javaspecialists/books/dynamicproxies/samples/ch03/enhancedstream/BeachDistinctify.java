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

package eu.javaspecialists.books.dynamicproxies.samples.ch03.enhancedstream;


import java.util.function.*;

/**
 * Described in The Java Specialists Newsletters:
 * https://www.javaspecialists.eu/archive/Issue274.html
 * https://www.javaspecialists.eu/archive/Issue275.html
 */
// tag::listing[]
public class BeachDistinctify {
  public static void main(String... args) {
    EnhancedStream.of("Kalathas", "Stavros", "STAVROS",
        "marathi", "kalathas", "baLos", "Balos")
        .distinct(String::toUpperCase, MERGE)
        .forEach(System.out::println);
  }

  // keep the string with the highest total ascii value
  public static final BinaryOperator<String> MERGE =
      (s1, s2) ->
          s1.chars().sum() < s2.chars().sum() ? s2 : s1;
}
// end::listing[]