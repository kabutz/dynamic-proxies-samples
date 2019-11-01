/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.ch03.perf;
// tag::listing[]
import eu.javaspecialists.books.dynamicproxies.*;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class MethodCallBenchmark {
  // direct call to RealTester
  private final RealTester realTester = new RealTester();

  // static proxies
  private final Tester staticProxy = new ProxyTester();
  private final Tester methodReference = realTester::increment;

  // dynamic proxies
  private final Tester dynamicProxyDirectCall =
      Proxies.castProxy(Tester.class,
          (proxy, method, args) ->
              realTester.increment());
  private final Tester dynamicProxyReflectiveCall =
      Proxies.castProxy(Tester.class,
          (proxy, method, args) ->
              method.invoke(realTester, args));

  @Benchmark
  public long directCall() {
    return realTester.increment();
  }
  @Benchmark
  public long staticProxy() {
    return staticProxy.increment();
  }
  @Benchmark
  public long methodReference() {
    return methodReference.increment();
  }
  @Benchmark
  public long dynamicProxyThenDirectCall() {
    return dynamicProxyDirectCall.increment();
  }
  @Benchmark
  public long dynamicProxyThenReflectiveCall() {
    return dynamicProxyReflectiveCall.increment();
  }

  public static void main(String... args) throws Exception {
    org.openjdk.jmh.Main.main(args);
  }
}
// end::listing[]
