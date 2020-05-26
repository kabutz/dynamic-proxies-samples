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

public class VirtualProxyDemo {
  public static void main(String... args) {
    // tag::listing[]
    CustomMap<String, Integer> map =
        Proxies.virtualProxy(CustomMap.class,
            CustomHashMap::new);
    System.out.println("Virtual Map created");
    map.put("one", 1); // creating CustomHashMap as side effect
    map.put("life", 42);
    System.out.println("map.get(\"life\") = " + map.get("life"));
    System.out.println("map.size() = " + map.size());
    System.out.println("clearing map");
    map.clear();
    System.out.println("map.size() = " + map.size());
    // end::listing[]
  }
}
