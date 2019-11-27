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
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class LoggingProxyDemo {
  public static void main(String... args) {
    Logger log = Logger.getGlobal();
    for (Handler handler : log.getParent().getHandlers()) {
      if (handler instanceof ConsoleHandler) {
        handler.setLevel(Level.FINE);
      }
    }
    log.setLevel(Level.FINE);

    // tag::listing[]
    var handler = new LoggingInvocationHandler(
        Logger.getGlobal(), new ConcurrentHashMap<>());
    @SuppressWarnings("unchecked")
    var map = (Map<String, Integer>)
                  Proxy.newProxyInstance(
                      Map.class.getClassLoader(),
                      new Class<?>[] {Map.class},
                      handler);
    map.put("one", 1);
    map.put("two", 2);
    System.out.println(map);
    map.clear();
    // end::listing[]
  }
}
