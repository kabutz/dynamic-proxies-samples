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
    this.adaptee = adaptee;
    this.adapter = adapter;

    var adapteeClass = adaptee.getClass();
    if (classNonPublic(adapteeClass) &&
            !target.isInstance(adaptee))
      throw new IllegalArgumentException(
          "Adaptee must either implement target interface" +
              " or be of a public class"
      );

    var adapterClass = adapter.getClass();
    if (classNonPublic(adapterClass)) {
      throw new IllegalArgumentException(
          "Adapter object must be of a public class");
    }

    var targetMethodMap = getMethodMap(target);
    adapteeMethodMap = getMethodMap(adapteeClass);
    adapterMethodMap = getMethodMap(adapterClass);

    if (classNonPublic(adapterClass)) {
      // use target interface methods
      adapterMethodMap.replaceAll(
          (key, value) -> targetMethodMap.get(key));
    }

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
  private boolean classNonPublic(Class<?> clazz) {
    return !Modifier.isPublic(clazz.getModifiers());
  }
  private Set<MethodKey> getMethodKeys(Class<?> clazz) {
    return Stream.of(clazz.getMethods())
               .map(MethodKey::new)
               .collect(Collectors.toSet());
  }
  private Map<MethodKey, Method> getMethodMap(Class<?> clazz) {
    return Stream.of(clazz.getMethods())
               .collect(Collectors.toMap(MethodKey::new,
                   Function.identity()));
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
      if (otherMethod != null)
        return otherMethod.invoke(adaptee, args);
      return method.invoke(adaptee, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
// end::listing[]