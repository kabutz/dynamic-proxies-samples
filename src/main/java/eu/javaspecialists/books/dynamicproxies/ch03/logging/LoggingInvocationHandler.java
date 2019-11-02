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

package eu.javaspecialists.books.dynamicproxies.ch03.logging;

import java.lang.reflect.*;
import java.util.logging.*;
import java.util.stream.*;

// tag::listing[]
public class LoggingInvocationHandler implements InvocationHandler {
  private final Logger log;
  private final Object obj;
  public LoggingInvocationHandler(Logger log, Object obj) {
    this.log = log;
    this.obj = obj;
  }
  @Override
  public Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    log.info(() -> "Entering " + toString(method, args));
    long start = nanoTime();
    try {
      return method.invoke(obj, args);
    } finally {
      long nanos = start == 0 ? 0 : nanoTime() - start;
      log.info(() -> "Exiting " + toString(method, args));
      log.fine(() -> "Execution took " + nanos + "ns");
    }
  }
  private long nanoTime() {
    // optimization - nanoTime() is an expensive native call
    return log.isLoggable(Level.FINE) ? System.nanoTime() : 0;
  }
  private final static Object[] EMPTY = {};
  private String toString(Method method, Object[] args) {
    if (args == null) args = EMPTY;
    return Stream.of(args).map(String::valueOf)
               .collect(Collectors.joining(", ",
                   method.getDeclaringClass().getName()
                       + "." + method.getName() + "(",
                   ")"));
  }
}
// end::listing[]
