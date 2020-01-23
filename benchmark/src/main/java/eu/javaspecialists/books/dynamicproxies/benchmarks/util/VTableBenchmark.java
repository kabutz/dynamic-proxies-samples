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

package eu.javaspecialists.books.dynamicproxies.benchmarks.util;

import eu.javaspecialists.books.dynamicproxies.util.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;
import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.*;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static junit.framework.TestCase.*;

/*
 -Deu.javaspecialists.books.dynamicproxies.util
 .ParameterTypesFetcher.enabled=true
 -Deu.javaspecialists.books.dynamicproxies.util
 .MethodTurboBooster.disabled=false
*/

/**
 * Will be converted to a JMH benchmark in the near future (or
 * not).
 */
@Fork(3)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class VTableBenchmark {
  static {
    try {
      System.out.println("===================================");
      Field parameter_fetcher =
          ParameterTypesFetcher.class.getDeclaredField(
              "PARAMETER_FETCHER");
      parameter_fetcher.setAccessible(true);
      System.out.println(parameter_fetcher.get(null)
                             .getClass().getSimpleName());
      Field booster =
          MethodTurboBooster.class.getDeclaredField(
              "BOOSTER");
      booster.setAccessible(true);
      System.out.println(booster.get(null)
                             .getClass().getSimpleName());
      System.out.println("===================================");
    } catch (ReflectiveOperationException e) {
      throw new Error(e);
    }
  }
  private static class TestData {
    private final Class<?> clazz;
    private final Object instance;
    private final MethodMap methodMap;
    private final VTable vtable;
    private final Method[] methods;
    public TestData(Object instance, Class<?> clazz) {
      this.clazz = clazz;
      this.instance = instance;
      methodMap = new MethodMap(instance.getClass(), clazz);
      vtable = VTables.newVTable(instance.getClass(), clazz);
      methods = methodMap.getMethods();
    }
  }

  private static TestData[] data = {
      new TestData(new ConcurrentSkipListSet<>(),
          NavigableSet.class),
      new TestData(new ArrayList<>(), List.class),
      new TestData(new HashSet<>(), Iterable.class),
  };

  static {
    System.out.println("Testing method map object creation");
    for (int i = 0; i < 1; i++) {
      for (TestData testData : data) {
        for (Method method : testData.methods) {
          assertEquals(method,
              testData.methodMap.lookup(method));
        }
      }
    }
    System.out.println("Testing vtable object creation");
    for (int i = 0; i < 1; i++) {
      for (TestData testData : data) {
        for (Method method : testData.methods) {
          assertEquals(method, testData.vtable.lookup(method));
        }
      }
    }
  }

  @Benchmark
  public void methodMapNavigableSet(Blackhole bh) {
    testMethodMap(bh, data[0]);
  }
  @Benchmark
  public void methodMapList(Blackhole bh) {
    testMethodMap(bh, data[1]);
  }
  @Benchmark
  public void methodMapIterable(Blackhole bh) {
    testMethodMap(bh, data[2]);
  }

  private static void testMethodMap(Blackhole bh,
                                    TestData testData) {
    for (int i = 0; i < testData.methods.length; i++) {
      Method lookup =
          testData.methodMap.lookup(testData.methods[i]);
      lookup.getClass(); // quick NPE check
      bh.consume(lookup);
    }
  }

  @Benchmark
  public void vtableNavigableSet(Blackhole bh) {
    testVTable(bh, data[0]);
  }
  @Benchmark
  public void vtableList(Blackhole bh) {
    testVTable(bh, data[1]);
  }
  @Benchmark
  public void vtableIterable(Blackhole bh) {
    testVTable(bh, data[2]);
  }

  private static void testVTable(Blackhole bh,
                                 TestData testData) {
    for (int i = 0; i < testData.methods.length; i++) {
      Method lookup =
          testData.vtable.lookup(testData.methods[i]);
      lookup.getClass(); // quick NPE check
      bh.consume(lookup);
    }
  }

  private static Map<MethodKey, Method> makeMethodMap(
      Class<?> receiver, Class<?> target) {
    return Stream.of(target.getMethods())
               .filter(method ->
                           !Modifier.isStatic(
                               method.getModifiers()
                           ))
               .collect(Collectors.toMap(MethodKey::new,
                   method -> {
                     try {
                       return receiver.getMethod(
                           method.getName(),
                           method.getParameterTypes());
                     } catch (NoSuchMethodException e) {
                       throw new AssertionError(e);
                     }
                   },
                   (method1, method2) -> {
                     var r1 = method1.getReturnType();
                     var r2 = method2.getReturnType();
                     if (r2.isAssignableFrom(r1)) {
                       return method1;
                     } else {
                       return method2;
                     }
                   }));
  }

  public static void main(String... args) throws RunnerException {
    Options normal = new OptionsBuilder()
                         .include(MethodHandles.lookup().lookupClass().getName())
                         .forks(3)
                         .warmupIterations(5)
                         .warmupTime(TimeValue.seconds(3))
                         .measurementIterations(10)
                         .measurementTime(TimeValue.seconds(3))
                         // .addProfiler("gc")
                         .jvmArgsAppend(
                             "-Deu.javaspecialists.books" +
                                 ".dynamicproxies.util" +
                                 ".ParameterTypesFetcher" +
                                 ".enabled=false")
                         .build();
    new Runner(normal).run();
    Options fast = new OptionsBuilder()
                       .include(MethodHandles.lookup().lookupClass().getName())
                       .forks(3)
                       .warmupIterations(5)
                       .warmupTime(TimeValue.seconds(3))
                       .measurementIterations(10)
                       .measurementTime(TimeValue.seconds(3))
                       // .addProfiler("gc")
                       .jvmArgsAppend(
                           "-Deu.javaspecialists.books" +
                               ".dynamicproxies.util" +
                               ".ParameterTypesFetcher" +
                               ".enabled=true")
                       .build();
    new Runner(fast).run();
    /*
      (Fast) is with FastParameterFetcher enabled

      Benchmark                        Score     Error   Units

      methodMapIterable                 43.151 ±  3.707   ns/op
      methodMapIterable (Fast)          26.154 ±  2.466   ns/op
      vtableIterable                    18.347 ±  1.130   ns/op
      vtableIterable (Fast)             18.309 ±  1.593   ns/op

      methodMapList                    483.700 ± 39.593   ns/op
      methodMapList (Fast)             312.515 ± 18.457   ns/op
      vtableList                       312.010 ± 11.920   ns/op
      vtableList (Fast)                245.992 ± 10.409   ns/op

      methodMapNavigableSet            541.828 ± 30.516   ns/op
      methodMapNavigableSet (Fast)     349.836 ± 23.869   ns/op
      vtableNavigableSet               339.409 ± 17.466   ns/op
      vtableNavigableSet (Fast)        301.123 ±  3.955   ns/op
     */
  }

}
