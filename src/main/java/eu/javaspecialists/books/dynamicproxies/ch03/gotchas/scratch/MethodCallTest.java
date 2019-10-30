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

package eu.javaspecialists.books.dynamicproxies.ch03.gotchas.scratch;

import eu.javaspecialists.books.dynamicproxies.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class MethodCallTest {
   private static final AtomicLong counter = new AtomicLong();

   public static void main(String... args) {
      for (int i = 0; i < 30; i++) {
         test();
      }
   }

   private static void test() {
      Tester[] testers = {
            Proxies.simpleProxy(Tester.class, new RealTester()),
            Proxies.virtualProxy(Tester.class, RealTester::new),
            Proxies.castProxy(Tester.class,
                  (proxy, method, args1) -> increment()),
            MethodCallTest::increment,
            () -> increment(),
            new RealTester(),
      };
      ThreadLocalRandom.current().ints(1_000_000 * testers.length, 0,
            testers.length)
            .forEach(i -> testers[i].increment());
      System.out.println("counter = " + counter);

      long[] times = new long[testers.length];
      for (int repeat = 0; repeat < 10; repeat++) {
         for (int index = 0; index < testers.length; index++) {
            Tester tester = testers[index];
            long time = System.nanoTime();
            try {
               for (int i = 0; i < 10_000_000; i++) {
                  tester.increment();
               }
            } finally {
               time = System.nanoTime() - time;
               times[index] += time;
            }
         }
      }
      for (int i = 0; i < testers.length; i++) {
         Tester tester = testers[i];
         System.out.printf("%s %dms%n",
               tester.getClass().getCanonicalName()
               , times[i] / 1_000_000);
      }
   }

   public interface Tester {
      long increment();
   }

   public static class RealTester implements Tester {
      @Override
      public long increment() {
         return MethodCallTest.increment();
      }
   }

   public static long increment() {
      return counter.incrementAndGet();
   }
}
