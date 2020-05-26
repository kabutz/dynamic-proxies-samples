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

package eu.javaspecialists.books.dynamicproxies.chapters.ch04.immutablecollection;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// tag::listing[]
public class HandcodedFilter<E>
    implements ImmutableCollection<E> {
  private final Collection<E> c;

  public HandcodedFilter(Collection<E> c) {
    this.c = c;
  }

  @Override
  public int size() {
    return c.size();
  }
  @Override
  public boolean isEmpty() {
    return c.isEmpty();
  }
  @Override
  public boolean contains(Object o) {
    return c.contains(o);
  }
  @Override
  public Object[] toArray() {
    return c.toArray();
  }
  @Override
  public <T> T[] toArray(T[] a) {
    return c.toArray(a);
  }
  @Override
  public <T> T[] toArray(IntFunction<T[]> generator) {
    return c.toArray(generator);
  }
  @Override
  public boolean containsAll(Collection<?> c) {
    return this.c.containsAll(c);
  }
  @Override
  public Spliterator<E> spliterator() {
    return c.spliterator();
  }
  @Override
  public Stream<E> stream() {
    return c.stream();
  }
  @Override
  public Stream<E> parallelStream() {
    return c.parallelStream();
  }
  @Override
  public void forEach(Consumer<? super E> action) {
    c.forEach(action);
  }
}
// end::listing[]

