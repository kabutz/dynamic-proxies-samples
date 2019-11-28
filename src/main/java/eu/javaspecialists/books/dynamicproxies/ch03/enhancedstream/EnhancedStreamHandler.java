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

/**
 * Described in The Java Specialists Newsletters:
 * https://www.javaspecialists.eu/archive/Issue274.html
 * https://www.javaspecialists.eu/archive/Issue275.html
 */
// tag::listing[]
public class EnhancedStreamHandler<T>
    implements InvocationHandler {
  private Stream<T> delegate;

  public EnhancedStreamHandler(Stream<T> delegate) {
    this.delegate = delegate;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    if (method.getReturnType() == EnhancedStream.class) {
      if (method.equals(enhancedDistinct)) {
        distinct((ToIntFunction<T>) args[0],
            (BiPredicate<T, T>) args[1],
            (BinaryOperator<T>) args[2]);
      } else if (method.equals(enhancedDistinctWithKey)) {
        distinct((Function<T, ?>) args[0],
            (BinaryOperator<T>) args[1]);
      } else {
        Method match = methodMap.get(method);
        this.delegate = (Stream<T>) match.invoke(delegate, args);
      }
      return proxy;
    } else {
      return method.invoke(this.delegate, args);
    }
  }

  private void distinct(ToIntFunction<T> hashCode,
                        BiPredicate<T, T> equals,
                        BinaryOperator<T> merger) {
    distinct(t -> new Key<>(t, hashCode, equals), merger);
  }
  private void distinct(Function<T, ?> keyGen,
                        BinaryOperator<T> merger) {
    delegate = delegate.collect(Collectors.toMap(keyGen::apply,
        Function.identity(), merger, LinkedHashMap::new))
                   .values()
                   .stream();
  }

  private static final class Key<E> {
    private final E e;
    private final ToIntFunction<E> hashCode;
    private final BiPredicate<E, E> equals;

    public Key(E e, ToIntFunction<E> hashCode,
               BiPredicate<E, E> equals) {
      this.e = e;
      this.hashCode = hashCode;
      this.equals = equals;
    }

    @Override
    public int hashCode() {
      return hashCode.applyAsInt(e);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Key)) return false;
      @SuppressWarnings("unchecked")
      Key<E> that = (Key<E>) obj;
      return equals.test(this.e, that.e);
    }
  }

  private static final Method enhancedDistinct;
  private static final Method enhancedDistinctWithKey;

  static {
    try {
      enhancedDistinct = EnhancedStream.class.getMethod(
          "distinct", ToIntFunction.class, BiPredicate.class,
          BinaryOperator.class);
      enhancedDistinctWithKey = EnhancedStream.class.getMethod(
          "distinct", Function.class, BinaryOperator.class);
    } catch (NoSuchMethodException e) {
      throw new Error(e);
    }
  }

  // methodMap contains a map from all non-static Stream methods
  // to the matching EnhancedStream methods
  private static final Map<Method, Method> methodMap =
      Stream.of(Stream.class.getMethods())
          .filter(m -> !Modifier.isStatic(m.getModifiers()))
          .collect(Collectors.toUnmodifiableMap(
              EnhancedStreamHandler::getEnhancedStreamMethod,
              Function.identity()));

  // since EnhancedStream is a subclass of Stream, it has to
  // contain all the methods of Stream.  We can safely do the
  // lookup, throwing an Error if we cannot find a method
  private static Method getEnhancedStreamMethod(Method m) {
    try {
      return EnhancedStream.class.getMethod(
          m.getName(), m.getParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new Error(e);
    }
  }
}
// end::listing[]