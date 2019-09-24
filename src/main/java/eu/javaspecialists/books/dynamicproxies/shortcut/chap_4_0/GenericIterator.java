/*
 * Copyright (c) 2014. Heinz Max Kabutz , Sven Ruppert
 *   We licenses
 *   this file to you under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License. You may
 *   obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.shortcut.chap_4_0;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by Sven Ruppert on 04.01.14.
 */

public class GenericIterator implements InvocationHandler {
  private final Iterator backing;
  private final Class type;
  private final Class typeIterator;

  public GenericIterator(Iterator backing, Class type) {
    this.backing = backing;
    this.type = type;
    typeIterator = discoverIterator();
  }

  /**
   * Find the correct inner class Iterator interface.
   *
   * @throws IllegalArgumentException if inner interface Iterator
   *                                  not found
   */
  private Class discoverIterator() {
    Class[] innerClasses = type.getClasses();
    for (Class innerClass : innerClasses) {
      if (innerClass.getName().equals(
        type.getName() + "$Iterator")) {
        return innerClass;
      }
    }
    throw new IllegalArgumentException(
      "Class does not contain inner Iterator interface");
  }

  public Class getTypeIteratorClass() {
    return typeIterator;
  }

  public Object invoke(Object proxy, Method method, Object[] args)
    throws Throwable {
    final String methodName = method.getName();
    if (methodName.equalsIgnoreCase("next" + type.getSimpleName())) {
      return backing.next();
    }
    return method.invoke(backing, args);
  }
}
