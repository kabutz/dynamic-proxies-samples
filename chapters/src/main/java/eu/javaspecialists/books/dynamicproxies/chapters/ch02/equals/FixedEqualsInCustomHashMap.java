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

package eu.javaspecialists.books.dynamicproxies.chapters.ch02.equals;

import eu.javaspecialists.books.dynamicproxies.chapters.ch02.virtual.*;

import java.util.*;
import java.util.function.*;

public class FixedEqualsInCustomHashMap {
  public static class CustomHashMap<K, V> implements CustomMap<K, V> {
    private final Map<K, V> map = new HashMap<>();
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
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
      map.forEach(action);
    }
    // tag::listing[]
    @Override
    public final boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof CustomMap)) return false;

      if (o instanceof CustomHashMap) {
        CustomHashMap<?, ?> that = (CustomHashMap<?, ?>) o;
        return this.map.equals(that.map);
      }
      return o.equals(this); // reversing the equals() condition
    }
    @Override
    public final int hashCode() {
      return map.hashCode();
    }
    // end::listing[]
  }
}