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

package eu.javaspecialists.books.dynamicproxies.util.benchmarks;

import eu.javaspecialists.books.dynamicproxies.util.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class MethodMap {
  private final Map<MethodKey, Method> map;

  public MethodMap(Class<?> receiver, Class<?> target) {
    map = Stream.of(target.getMethods())
              .filter(method ->
                          !Modifier.isStatic(
                              method.getModifiers()
                          ))
              .collect(Collectors.toMap(MethodKey::new,
                  method -> {
                    try {
                      return receiver.getMethod(
                          method.getName(),
                          method.getParameterTypes());
                    } catch (NoSuchMethodException e) {
                      throw new AssertionError(e);
                    }
                  },
                  (method1, method2) -> {
                    var r1 = method1.getReturnType();
                    var r2 = method2.getReturnType();
                    if (r2.isAssignableFrom(r1)) {
                      return method1;
                    } else {
                      return method2;
                    }
                  }));
  }

  public Method lookup(Method method) {
    MethodKey key = new MethodKey(method);
    return map.get(key);
  }

  public Method[] getMethods() {
    return map.values().toArray(Method[]::new);
  }
}
