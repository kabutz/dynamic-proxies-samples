/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
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

package eu.javaspecialists.books.dynamicproxies.ch03.enhancedstream;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// tag::listing[]
/**
 * Described in The Java Specialists Newsletters:
 * https://www.javaspecialists.eu/archive/Issue274.html
 * https://www.javaspecialists.eu/archive/Issue275.html
 */
public interface EnhancedStream<T> extends Stream<T> {
  // new enhanced distinct()
  EnhancedStream<T> distinct(ToIntFunction<T> hashCode,
                             BiPredicate<T, T> equals,
                             BinaryOperator<T> merger);
  EnhancedStream<T> distinct(Function<T, ?> keyGenerator,
                                 BinaryOperator<T> merger);

  // old methods with enhanced return type
  EnhancedStream<T> filter(Predicate<? super T> predicate);
  <R> EnhancedStream<R> map(
      Function<? super T, ? extends R> mapper);
  <R> EnhancedStream<R> flatMap(
      Function<? super T, ? extends Stream<? extends R>> mapper);
  EnhancedStream<T> distinct();
  EnhancedStream<T> sorted();
  EnhancedStream<T> sorted(Comparator<? super T> comparator);
  EnhancedStream<T> peek(Consumer<? super T> action);
  EnhancedStream<T> limit(long maxSize);
  EnhancedStream<T> skip(long n);
  EnhancedStream<T> takeWhile(Predicate<? super T> predicate);
  EnhancedStream<T> dropWhile(Predicate<? super T> predicate);
  EnhancedStream<T> sequential();
  EnhancedStream<T> parallel();
  EnhancedStream<T> unordered();
  EnhancedStream<T> onClose(Runnable closeHandler);

  // static factory methods
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <E> EnhancedStream<E> of(E... elements) {
    return from(Stream.of(elements));
  }
  @SuppressWarnings("unchecked")
  static <E> EnhancedStream<E> from(Stream<E> stream) {
    return (EnhancedStream<E>) Proxy.newProxyInstance(
        EnhancedStream.class.getClassLoader(),
        new Class<?>[] {EnhancedStream.class},
        new EnhancedStreamHandler<>(stream)
    );
  }
}
// end::listing[]