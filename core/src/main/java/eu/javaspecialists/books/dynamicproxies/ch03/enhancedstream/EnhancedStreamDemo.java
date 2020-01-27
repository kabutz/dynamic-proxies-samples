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

package eu.javaspecialists.books.dynamicproxies.ch03.enhancedstream;


import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Described in The Java Specialists Newsletters 274 and 275
 * https://www.javaspecialists.eu/archive/Issue274.html
 * https://www.javaspecialists.eu/archive/Issue275.html
 */
// tag::listing[]
public class EnhancedStreamDemo {
  public static void main(String... args) {
    System.out.println("Normal ArrayDeque clone() Methods:");
    Stream.of(ArrayDeque.class.getMethods())
        .filter(method -> method.getName().equals("clone"))
        .distinct()
        .forEach(EnhancedStreamDemo::print);

    System.out.println();

    System.out.println("Distinct ArrayDeque:");
    EnhancedStream.of(ArrayDeque.class.getMethods())
        .filter(method -> method.getName().equals("clone"))
        .distinct(HASH_CODE, EQUALS, MERGE)
        .forEach(EnhancedStreamDemo::print);

    System.out.println();

    System.out.println("Normal ConcurrentSkipListSet:");
    Stream.of(ConcurrentSkipListSet.class.getMethods())
        .filter(method -> method.getName().contains("Set"))
        .distinct()
        .sorted(METHOD_COMPARATOR)
        .forEach(EnhancedStreamDemo::print);

    System.out.println();

    System.out.println("Distinct ConcurrentSkipListSet:");
    EnhancedStream.of(ConcurrentSkipListSet.class.getMethods())
        .filter(method -> method.getName().contains("Set"))
        .distinct(HASH_CODE, EQUALS, MERGE)
        .sorted(METHOD_COMPARATOR)
        .forEach(EnhancedStreamDemo::print);
  }

  private static void print(Method m) {
    System.out.println(
        Stream.of(m.getParameterTypes())
            .map(Class::getSimpleName)
            .collect(Collectors.joining(
                ", ",
                "  " + m.getReturnType().getSimpleName()
                    + " " + m.getName() + "(",
                ")"))
    );
  }

  public static final ToIntFunction<Method> HASH_CODE =
      method -> method.getName().hashCode() +
                    method.getParameterCount();

  public static final BiPredicate<Method, Method> EQUALS =
      (method1, method2) ->
          method1.getName().equals(method2.getName()) &&
              method1.getParameterCount() ==
                  method2.getParameterCount() &&
              Arrays.equals(method1.getParameterTypes(),
                  method2.getParameterTypes());

  public static final BinaryOperator<Method> MERGE =
      (method1, method2) -> {
        if (method1.getReturnType()
                .isAssignableFrom(method2.getReturnType()))
          return method2;
        if (method2.getReturnType()
                .isAssignableFrom(method1.getReturnType()))
          return method1;
        throw new IllegalArgumentException(
            "Conflicting return types " +
                method1.getReturnType().getCanonicalName() +
                " and " +
                method2.getReturnType().getCanonicalName());
      };

  public static final Comparator<Method> METHOD_COMPARATOR =
      Comparator.comparing(Method::getName)
          .thenComparing(method ->
                             Arrays.toString(
                                 method.getParameterTypes()));
}
// end::listing[]

