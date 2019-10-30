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

package eu.javaspecialists.books.dynamicproxies.ch04;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// tag::HandcodedFilter[]
public class HandcodedFilter<E> implements ImmutableCollection<E> {
   private final Collection<E> c;

   public HandcodedFilter(Collection<E> c) {
      this.c = c;
   }

   public int size() {
      return c.size();
   }
   public boolean isEmpty() {
      return c.isEmpty();
   }
   public boolean contains(Object o) {
      return c.contains(o);
   }
   public Object[] toArray() {
      return c.toArray();
   }
   public <T> T[] toArray(T[] a) {
      return c.toArray(a);
   }
   public <T> T[] toArray(IntFunction<T[]> generator) {
      return c.toArray(generator);
   }
   public boolean containsAll(Collection<?> c) {
      return this.c.containsAll(c);
   }
   public Spliterator<E> spliterator() {
      return c.spliterator();
   }
   public Stream<E> stream() {
      return c.stream();
   }
   public Stream<E> parallelStream() {
      return c.parallelStream();
   }
   public void forEach(Consumer<? super E> action) {
      c.forEach(action);
   }
}
// end::HandcodedFilter[]

