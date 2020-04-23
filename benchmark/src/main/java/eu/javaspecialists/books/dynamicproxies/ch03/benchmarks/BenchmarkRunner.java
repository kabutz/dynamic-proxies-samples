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

import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.*;

public class BenchmarkRunner {
  public static void main(String... args) throws RunnerException {
    String name = MethodCallBenchmark.class.getName();
    // Object Allocation with Escape Analysis ON
    new Runner(
        new OptionsBuilder()
            .include(name)
            .forks(1)
            .jvmArgsAppend(
                "-XX:+DoEscapeAnalysis", "-XX:+UseParallelGC")
            .warmupIterations(3)
            .warmupTime(TimeValue.seconds(1))
            .measurementIterations(3)
            .measurementTime(TimeValue.seconds(1))
            .addProfiler("gc")
            .build()).run();
    // Object Allocation with Escape Analysis OFF
    new Runner(
        new OptionsBuilder()
            .include(name)
            .forks(1)
            .jvmArgsAppend(
                "-XX:-DoEscapeAnalysis", "-XX:+UseParallelGC")
            .warmupIterations(3)
            .warmupTime(TimeValue.seconds(1))
            .measurementIterations(3)
            .measurementTime(TimeValue.seconds(1))
            .addProfiler("gc")
            .build()).run();
    // Object Allocation with Escape Analysis OFF no turbo boost
    new Runner(
        new OptionsBuilder()
            .include(name)
            .forks(1)
            .jvmArgsAppend(
                "-XX:-DoEscapeAnalysis",
                "-XX:+UseParallelGC",
                "-Deu.javaspecialists.books.dynamicproxies" +
                    ".util.MethodTurboBooster.disabled=true")
            .warmupIterations(3)
            .warmupTime(TimeValue.seconds(1))
            .measurementIterations(3)
            .measurementTime(TimeValue.seconds(1))
            .addProfiler("gc")
            .build()).run();

    new Runner(
        new OptionsBuilder()
            .include(name)
            .forks(3)
            .jvmArgsAppend(
                "-XX:+UseParallelGC",
                "-Deu.javaspecialists.books.dynamicproxies" +
                    ".util.MethodTurboBooster.disabled=false")
            .warmupIterations(5)
            .warmupTime(TimeValue.seconds(3))
            .measurementIterations(10)
            .measurementTime(TimeValue.seconds(3))
            .build()).run();
    new Runner(
        new OptionsBuilder()
            .include(name)
            .forks(3)
            .jvmArgsAppend(
                "-XX:+UseParallelGC",
                "-Deu.javaspecialists.books.dynamicproxies" +
                    ".util.MethodTurboBooster.disabled=true")
            .warmupIterations(5)
            .warmupTime(TimeValue.seconds(3))
            .measurementIterations(10)
            .measurementTime(TimeValue.seconds(3))
            .build()).run();
  }
}