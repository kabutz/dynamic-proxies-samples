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

import eu.javaspecialists.books.dynamicproxies.ch05.bettercollection.*;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

// tag::listing[]
@Fork(value = 3, jvmArgsAppend = {"-XX:+UseParallelGC",
    "-Deu.javaspecialists.books.dynamicproxies.util" +
        ".ParameterTypesFetcher.enabled=true"})
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

  private static final Predicate<String> predicate =
      s -> s.length() < 10;

  private static class Counter implements Consumer<Object> {
    private int count;
    @Override
    public void accept(Object o) { count++; }
    public void reset() { count = 0; }
    public int get() { return count; }
  }

  private static final Counter COUNTER = new Counter();

  @Benchmark
  public int plainForEach() {
    COUNTER.reset();
    Objects.requireNonNull(predicate, "predicate==null");
    Objects.requireNonNull(COUNTER, "action==null");
    for (String e : plain) {
      if (predicate.test(e)) COUNTER.accept(e);
    }
    return COUNTER.get();
  }

  @Benchmark
  public int classAdapterForEach() {
    COUNTER.reset();
    classAdapter.forEachFiltered(predicate, COUNTER);
    return COUNTER.get();
  }
  @Benchmark
  public int objectAdapterForEach() {
    COUNTER.reset();
    objectAdapter.forEachFiltered(predicate, COUNTER);
    return COUNTER.get();
  }

  @Benchmark
  public int dynamicObjectAdapterForEach() {
    COUNTER.reset();
    dynamicObjectAdapter.forEachFiltered(predicate, COUNTER);
    return COUNTER.get();
  }

  @Benchmark
  public Iterator<String> iterator() {
    return plain.iterator();
  }

  @Benchmark
  public Object[] parameterArray() {
    return new Object[] {predicate, COUNTER};
  }
}
// end::listing[]
