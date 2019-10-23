/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.ch02.virtual;

// tag::listing[]
public class VirtualProxyTest {
    public static void main(String... args) {
        CustomMap<String, Integer> map =
                new VirtualCustomMap<>(CustomHashMap::new);
        System.out.println("Virtual Map created");
        map.put("one", 1);
        map.put("life", 42);
        System.out.println("map.get(\"life\") = " + map.get("life"));
        System.out.println("map.size() = " + map.size());
        System.out.println("clearing map");
        map.clear();
        System.out.println("map.size() = " + map.size());
    }
}
// end::listing[]
