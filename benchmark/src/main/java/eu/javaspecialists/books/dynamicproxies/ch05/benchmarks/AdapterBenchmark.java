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

package eu.javaspecialists.books.dynamicproxies.ch05.benchmarks;

/*
 -Deu.javaspecialists.books.dynamicproxies.util
 .ParameterTypesFetcher.enabled=true
 -Deu.javaspecialists.books.dynamicproxies.util
 .MethodTurboBooster.disabled=false

 */
import eu.javaspecialists.books.dynamicproxies.ch05.bettercollection.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.*;

import java.lang.invoke.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

// tag::listing[]
@Fork(3)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class AdapterBenchmark {
  private final Collection<String> plain =
      new ConcurrentSkipListSet<>();

  private final BetterCollection<String> classAdapter =
      new BetterConcurrentSkipListSet<>(new String[0]);

  private final BetterCollection<String> objectAdapter =
      new BetterCollectionObjectAdapter<>(
          new ConcurrentSkipListSet<>(), new String[0]);

  private final BetterCollection<String> dynamicObjectAdapter =
      BetterCollectionFactory.asBetterCollection(
          new ConcurrentSkipListSet<>(), new String[0]
      );

  private final Collection<Collection<String>> all =
      List.of(plain, classAdapter, objectAdapter,
          dynamicObjectAdapter);


  @Setup
  public void init() {
    all.forEach(Collection::clear);

    all.forEach(c -> c.add("John"));
    all.forEach(c -> c.add("Mary"));
    all.forEach(c -> c.add("Menongahela"));
    all.forEach(c -> c.add("Bobby Tables"));
  }

  @Benchmark
  public int plainSize() {
    return plain.size();
  }
  @Benchmark
  public int classAdapterSize() {
    return classAdapter.size();
  }
  @Benchmark
  public int objectAdapterSize() {
    return objectAdapter.size();
  }
  @Benchmark
  public int dynamicObjectAdapterSize() {
    return dynamicObjectAdapter.size();
  }

  @Benchmark
  public Object[] plainToArray() {
    return plain.toArray();
  }
  @Benchmark
  public String[] classAdapterToArray() {
    return classAdapter.toArray();
  }
  @Benchmark
  public String[] objectAdapterToArray() {
    return objectAdapter.toArray();
  }
  @Benchmark
  public String[] dynamicObjectAdapterToArray() {
    return dynamicObjectAdapter.toArray();
  }

  private static final Predicate<String> shortNames =
      s -> s.length() < 10;

  @Benchmark
  public void plainForEach() {
    plain.stream().filter(shortNames).forEach(
        Objects::requireNonNull);
  }
  @Benchmark
  public void classAdapterForEach() {
    classAdapter.forEachFiltered(
        shortNames, Objects::requireNonNull);
  }
  @Benchmark
  public void objectAdapterForEach() {
    objectAdapter.forEachFiltered(
        shortNames, Objects::requireNonNull);
  }
  @Benchmark
  public void dynamicObjectAdapterForEach() {
    dynamicObjectAdapter.forEachFiltered(
        shortNames, Objects::requireNonNull);
  }

  public static void main(String... args) throws RunnerException {
    Options opt = new OptionsBuilder()
                      .include(MethodHandles.lookup().lookupClass().getName())
                      .forks(3)
                      .warmupIterations(5)
                      .warmupTime(TimeValue.seconds(3))
                      .measurementIterations(10)
                      .measurementTime(TimeValue.seconds(3))
                      .addProfiler("gc")
                      .build();
    new Runner(opt).run();
  }
}
// end::listing[]
