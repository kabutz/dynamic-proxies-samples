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

package eu.javaspecialists.books.dynamicproxies.ch03;

import eu.javaspecialists.books.dynamicproxies.*;

import java.util.function.*;

public class VirtualProxyTest {
    // tag::Map[]
    public interface Map<K, V> {
        int size();
        V get(Object key);
        V put(K key, V value);
        V remove(Object key);
        void clear();
        // etc.
    }
    // end::Map[]
    static
    // tag::HashMap[]
    public class HashMap<K, V> implements Map<K, V> {
        private final java.util.HashMap<K, V> map =
                new java.util.HashMap<>();
        public HashMap() {
            System.out.println("HashMap constructed");
        }
        @Override
        public int size() {
            return map.size();
        }
        @Override
        public V get(Object key) {
            return map.get(key);
        }
        @Override
        public V put(K key, V value) {
            return map.put(key, value);
        }
        @Override
        public V remove(Object key) {
            return map.remove(key);
        }
        @Override
        public void clear() {
            map.clear();
        }
    }
    // end::HashMap[]
    static
    // tag::VirtualMap[]
    public class VirtualMap<K, V> implements Map<K, V> {
        private final Supplier<Map<K, V>> mapSupplier;
        private Map<K, V> realMap;
        public VirtualMap(Supplier<Map<K, V>> mapSupplier) {
            this.mapSupplier = mapSupplier;
        }
        private Map<K, V> getRealMap() { // not thread-safe
            if (realMap == null) realMap = mapSupplier.get();
            return realMap;
        }
        @Override
        public int size() {
            return getRealMap().size();
        }
        @Override
        public V get(Object key) {
            return getRealMap().get(key);
        }
        @Override
        public V put(K key, V value) {
            return getRealMap().put(key, value);
        }
        @Override
        public V remove(Object key) {
            return getRealMap().remove(key);
        }
        @Override
        public void clear() {
            getRealMap().clear();
        }
    }
    // end::VirtualMap[]

    static
    public class Main {
        public static void main(String... args) {
            // tag::main()[]
            Map<String, Integer> map =
                    Proxies.virtualProxy(Map.class, HashMap::new);
            System.out.println("Virtual Map created");
            map.put("one", 1);
            map.put("life", 42);
            System.out.println("map.get(\"life\") = " + map.get("life"));
            map.clear();
            System.out.println("map.size() = " + map.size());
            // end::main()[]
        }
    }
}
