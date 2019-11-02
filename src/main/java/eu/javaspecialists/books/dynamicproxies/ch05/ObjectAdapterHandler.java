/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max
 * Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the
 *  "License");
 * you may not use this file except in compliance with the
 * License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software
 * distributed under the License is distributed on an "AS IS"
 * BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing
 * permissions and
 * limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.ch05;

import eu.javaspecialists.books.dynamicproxies.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// tag::listing[]
public class ObjectAdapterHandler implements InvocationHandler {
  private final Object adaptee;
  private final Object adapter;
  private final Map<MethodKey, Method> adapteeMethodMap;
  private final Map<MethodKey, Method> adapterMethodMap;
  public ObjectAdapterHandler(Class<?> target,
                              Object adaptee,
                              Object adapter) {
    checkClassPublic(adaptee.getClass());
    checkClassPublic(adapter.getClass());

    this.adaptee = adaptee;
    this.adapter = adapter;

    adapterMethodMap = getMethodMap(adapter.getClass(), true);
    System.out.println(adapterMethodMap);
    adapteeMethodMap = getMethodMap(adaptee.getClass(), false);
    System.out.println(adapteeMethodMap);

    checkTargetMethodsImplemented(target);
  }

  @Override
  public Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    try {
      var key = new MethodKey(method);
      var otherMethod = adapterMethodMap.get(key);
      if (otherMethod != null) {
        return otherMethod.invoke(adapter, args);
      }
      otherMethod = adapteeMethodMap.get(key);
      return otherMethod.invoke(adaptee, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void checkClassPublic(Class<?> clazz) {
    if (!Modifier.isPublic(clazz.getModifiers()))
      throw new IllegalArgumentException(
          clazz + " needs to be public");
  }

  private Map<MethodKey, Method> getMethodMap(
      Class<?> clazz, boolean ownMethodsOnly) {
    Predicate<Method> includeFilter;
    if (ownMethodsOnly)
      includeFilter = m -> m.getDeclaringClass() == clazz;
    else
      includeFilter = m -> true;
    return
        Stream.of(clazz.getMethods())
            .filter(includeFilter)
            .collect(Collectors.toMap(MethodKey::new,
                Function.identity(),
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

  private void checkTargetMethodsImplemented(Class<?> target) {
    var targetMethodMap = getMethodMap(target, false);
    targetMethodMap.keySet().removeAll(
        adapteeMethodMap.keySet()
    );
    targetMethodMap.keySet().removeAll(
        adapterMethodMap.keySet()
    );
    if (!targetMethodMap.isEmpty())
      throw new IllegalArgumentException(
          "Target methods not implemented: " +
              targetMethodMap.keySet());
  }
}
// end::listing[]