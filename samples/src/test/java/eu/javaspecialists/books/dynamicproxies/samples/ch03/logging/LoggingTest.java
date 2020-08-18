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

package eu.javaspecialists.books.dynamicproxies.samples.ch03.logging;

import eu.javaspecialists.books.dynamicproxies.*;
import org.junit.*;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.logging.*;

import static org.junit.Assert.*;

public class LoggingTest {
  @Test
  public void testLoggingWithTime() {
    testLogging(true, 3);
  }

  @Test
  public void testLoggingWithoutTime() {
    testLogging(false, 2);
  }

  private void testLogging(boolean withFine, int expectedCount) {
    AtomicInteger logCount = new AtomicInteger();

    Logger logger = Logger.getGlobal();
    for (Handler handler : logger.getParent().getHandlers()) {
      if (handler instanceof ConsoleHandler) {
        if (withFine)  handler.setLevel(Level.FINE);
        handler.setFilter(record -> {
          logCount.incrementAndGet();
          return true;
        });
      }
    }
    if (withFine) logger.setLevel(Level.FINE);

    List<Integer> digitPrimes = List.of(2, 3, 5, 7);

    List<Integer> numbersProxy = Proxies.castProxy(
        List.class,
        new LoggingInvocationHandler(logger, digitPrimes));

    // Call the proxy, checking that the handler invokes size()
    assertEquals(digitPrimes.size(), numbersProxy.size());

    // and that the logger is called
    assertEquals(expectedCount, logCount.get());
  }

}
