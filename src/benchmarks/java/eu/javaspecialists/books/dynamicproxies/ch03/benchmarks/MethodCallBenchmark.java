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

package eu.javaspecialists.books.dynamicproxies.ch03.benchmarks;

import eu.javaspecialists.books.dynamicproxies.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.*;

import java.lang.invoke.*;
import java.util.concurrent.*;

/*
 -Deu.javaspecialists.books.dynamicproxies.util.ParameterTypesFetcher.enabled=true
 -Deu.javaspecialists.books.dynamicproxies.util.MethodTurboBooster.disabled=false
*/

// tag::listing[]
@Fork(3)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)

public class MethodCallBenchmark {
  // direct call to RealWorker
  private final RealWorker realWorker = new RealWorker();

  // static proxies
  private final Worker staticProxy = new ProxyWorker();

  //   dynamic proxies
  private final Worker dynamicProxyDirectCallIncrement =
      Proxies.castProxy(Worker.class,
          (proxy, method, args) -> realWorker.increment());
  private final Worker dynamicProxyDirectCallConsumeCPU =
      Proxies.castProxy(Worker.class,
          (proxy, method, args) -> {
            realWorker.consumeCPU();
            return null;
          });
  private final Worker dynamicProxyReflectiveCall =
      Proxies.castProxy(Worker.class,
          (proxy, method, args) ->
              method.invoke(realWorker, args));

  @Benchmark
  public long directCallIncrement() {
    return realWorker.increment();
  }
  @Benchmark
  public long staticProxyIncrement() {
    return staticProxy.increment();
  }
  @Benchmark
  public long dynamicProxyDirectCallIncrement() {
    return dynamicProxyDirectCallIncrement.increment();
  }
  @Benchmark
  public long dynamicProxyReflectiveCallIncrement() {
    return dynamicProxyReflectiveCall.increment();
  }

  @Benchmark
  public void directCallConsumeCPU() {
    realWorker.consumeCPU();
  }
  @Benchmark
  public void staticProxyConsumeCPU() {
    staticProxy.consumeCPU();
  }
  @Benchmark
  public void dynamicProxyDirectCallConsumeCPU() {
    dynamicProxyDirectCallConsumeCPU.consumeCPU();
  }
  @Benchmark
  public void dynamicProxyReflectiveCallConsumeCPU() {
    dynamicProxyReflectiveCall.consumeCPU();
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
