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

package eu.javaspecialists.books.dynamicproxies.util.benchmarks;

import eu.javaspecialists.books.dynamicproxies.util.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

public class VTableTest {
  public static final int REPEATS = 3_000_000;
  public static void main(String... args) {
    test(NavigableSet.class, new ConcurrentSkipListSet<>());
    test(List.class, new ArrayList<>());
    test(Iterable.class, new HashSet<>());
  }
  private static void test(Class<?> clazz, Object instance) {
    System.out.println("Testing " + clazz);
    Method[] methods = clazz.getMethods();

    Map<MethodKey, Method> methodMap =
        Stream.of(methods)
            .collect(Collectors.toMap(MethodKey::new,
                Function.identity(),
                (method1, method2) -> {
                  var r1 = method1.getReturnType();
                  var r2 = method2.getReturnType();
                  if (r2.isAssignableFrom(r1)) {
                    return method1;
                  } else {
                    return method2;
                  }
                }));

    Map<String, Long> countedNames =
        methodMap.values().stream()
            .collect(Collectors.groupingBy(Method::getName,
                Collectors.counting()));
    countedNames.values().removeIf(val -> val != 1L);
    System.out.println("countedNames = " + countedNames);

    VTable vtable = VTables.newVTable(instance.getClass(), clazz);

    methods = methodMap.values().toArray(Method[]::new);
    for (int i = 0; i < 10; i++) {
      test(methods, vtable);
//      test(methods, methodMap);
    }
  }

  private static void test(Method[] methods, VTable vtable) {
    long time = System.nanoTime();
    try {
      for (int i = 0; i < REPEATS; i++) {
        for (Method method : methods) {
          Method match = vtable.lookup(method);
//          if (!match.equals(method)) {
//            System.out.println("method = " + method);
//            System.out.println("match = " + match);
//            throw new AssertionError();
//          }
        }
      }
    } finally {
      time = System.nanoTime() - time;
      System.out.printf("vtable time = %dms%n",
          (time / 1_000_000));
    }
  }
  private static void test(Method[] methods,
                           Map<MethodKey, Method> methodMap) {
    long time = System.nanoTime();
    try {
      for (int i = 0; i < REPEATS; i++) {
        for (Method method : methods) {
          if (methodMap.get(new MethodKey(method)) != method)
            throw new AssertionError();
        }
      }
    } finally {
      time = System.nanoTime() - time;
      System.out.printf("methodmap time = %dms%n",
          (time / 1_000_000));
    }
  }
}
