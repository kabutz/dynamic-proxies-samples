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
public interface ImmutableCollection<E>
    extends ImmutableIterable<E> {
  int size();
  boolean isEmpty();
  boolean contains(Object o);
  Object[] toArray();
  <T> T[] toArray(T[] a);
  <T> T[] toArray(IntFunction<T[]> generator);
  boolean containsAll(Collection<?> c);
  Stream<E> stream();
  Stream<E> parallelStream();

  // to try out default methods
  default void printAll() {
    forEach(System.out::println);
  }

  // mutator methods from Collection filtered away
  //    boolean add(E e);
  //    boolean remove(Object o);
  //    boolean addAll(Collection<? extends E> c);
  //    boolean removeAll(Collection<?> c);
  //    boolean removeIf(Predicate<? super E> filter);
  //    boolean retainAll(Collection<?> c);
  //    void clear();
}
// end::listing[]

