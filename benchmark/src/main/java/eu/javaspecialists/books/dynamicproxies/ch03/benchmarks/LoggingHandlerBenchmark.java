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

package eu.javaspecialists.books.dynamicproxies.ch03.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.*;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

// tag::listing[]
@Fork(3)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class LoggingHandlerBenchmark {
  private static final Method EQUALS, HASH_CODE;
  private static final Object[] ARGS = {new Object()};
  static {
    try {
      EQUALS = Object.class.getMethod("equals", Object.class);
      HASH_CODE = Object.class.getMethod("hashCode");
    } catch (ReflectiveOperationException e) {
      throw new Error(e);
    }
  }

  @Benchmark
  public long nanoTime() {
    return System.nanoTime() + System.nanoTime();
  }

  @Benchmark
  public String toStringFormatStreamEquals() {
    return toStringFormatStream(EQUALS, ARGS);
  }
  @Benchmark
  public String toStringFormatStreamHashCode() {
    return toStringFormatStream(HASH_CODE, null);
  }

  private String toStringFormatStream(Method method,
                                      Object[] args) {
    return String.format("%s.%s(%s)",
        method.getDeclaringClass().getCanonicalName(),
        method.getName(),
        args == null ? "" :
            Stream.of(args).map(String::valueOf)
                .collect(Collectors.joining(", ")));
  }

  @Benchmark
  public String toStringPlusStreamEquals() {
    return toStringPlusStream(EQUALS, ARGS);
  }
  @Benchmark
  public String toStringPlusStreamHashCode() {
    return toStringPlusStream(HASH_CODE, null);
  }

  private String toStringPlusStream(Method method,
                                    Object[] args) {
    return
        method.getDeclaringClass().getCanonicalName() + "." +
            method.getName() + "(" +
            (args == null ? "" :
                 Stream.of(args).map(String::valueOf)
                     .collect(Collectors.joining(", ")) + ")");
  }

  @Benchmark
  public String toStringPlusJoinerEquals() {
    return toStringPlusJoiner(EQUALS, ARGS);
  }
  @Benchmark
  public String toStringPlusJoinerHashCode() {
    return toStringPlusJoiner(HASH_CODE, null);
  }

  private String toStringPlusJoiner(Method method,
                                    Object[] args) {
    StringJoiner joiner = new StringJoiner(", ",
        method.getDeclaringClass().getCanonicalName() + "." +
            method.getName() + "(",
        ")");
    if (args != null) {
      for (Object arg : args) {
        joiner.add(String.valueOf(arg));
      }
    }
    return joiner.toString();
  }

  public static void main(String... args) throws RunnerException {
    Options opt = new OptionsBuilder()
                      .include(MethodHandles.lookup().lookupClass().getName())
                      .forks(3)
                      .warmupIterations(5)
                      .warmupTime(TimeValue.seconds(1))
                      .measurementIterations(10)
                      .measurementTime(TimeValue.seconds(1))
                      .addProfiler("gc")
                      .build();
    new Runner(opt).run();
  }
}
// end::listing[]
