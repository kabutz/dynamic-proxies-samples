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

package eu.javaspecialists.books.dynamicproxies.ch02.protection;

import eu.javaspecialists.books.dynamicproxies.ch02.virtual.*;

import java.util.function.*;

// tag::listing[]
public class UnmodifiableCustomMap<K, V>
    implements CustomMap<K, V> {
  private final CustomMap<K, V> map;
  public UnmodifiableCustomMap(CustomMap<K, V> map) {
    this.map = map;
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
    throw new UnsupportedOperationException("unmodifiable");
  }
  @Override
  public V remove(Object key) {
    throw new UnsupportedOperationException("unmodifiable");
  }
  @Override
  public void clear() {
    throw new UnsupportedOperationException("unmodifiable");
  }
  @Override
  public void forEach(BiConsumer<? super K, ? super V> action) {
    map.forEach(action);
  }
}
// end::listing[]
