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

package eu.javaspecialists.books.dynamicproxies.util;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

// tag::listing[]
/**
 * MethodKey is used to compare Methods by name and parameter
 * types. It has equals(), hashCode(), compareTo() and toString()
 * implemented.  We can use it as a key in a map. MethodKey does
 * not know the return type of the method.
 */
public final class MethodKey implements Comparable<MethodKey> {
  private final String name;
  private final Class<?>[] paramTypes;

  public MethodKey(Method method) {
    name = method.getName();
    paramTypes = ParameterTypesFetcher.get(method);
  }

  public MethodKey(Class<?> clazz, String name,
                   Class<?>... paramTypes) {
    try {
      // check that method exists in the given class
      var method = clazz.getMethod(name, paramTypes);
      // method names are all interned in the JVM
      this.name = method.getName();
      this.paramTypes = Objects.requireNonNull(paramTypes);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MethodKey)) {
      return false;
    }
    // name and paramTypes cannot be null
    var other = (MethodKey) obj;
    return name == other.name && // method names are interned
               equalParamTypes(paramTypes, other.paramTypes);
  }

  /**
   * We compare classes using == instead of .equals().  We know
   * that the arrays will never be null.  We can thus avoid some
   * of the checks done in Arrays.equals().
   */
  private boolean equalParamTypes(Class<?>[] params1,
                                  Class<?>[] params2) {
    if (params1.length == params2.length) {
      for (int i = 0; i < params1.length; i++) {
        if (params1[i] != params2[i])
          return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode() + paramTypes.length;
  }

  @Override
  public int compareTo(MethodKey that) {
    int result = this.name.compareTo(that.name);
    if (result != 0) return result;
    return Arrays.compare(this.paramTypes,
        that.paramTypes,
        Comparator.comparing(Class::getName));
  }

  @Override
  public String toString() {
    return Stream.of(paramTypes)
               .map(Class::getName)
               .collect(Collectors.joining(", ",
                   name + "(", ")"));
  }
}
// end::listing[]

