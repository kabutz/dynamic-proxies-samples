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

package eu.javaspecialists.books.dynamicproxies.util;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

// tag::listing[]
public final class MethodKey implements Comparable<MethodKey> {
  private final String name;
  private final Class<?>[] parameterTypes;
  public MethodKey(Method method) {
    name = method.getName();
    parameterTypes = ParameterTypesFetcher.get(method);
  }
  public MethodKey(Class<?> clazz, String name,
                   Class<?>... parameterTypes) {
    try {
      var method = clazz.getMethod(name, parameterTypes);
      this.name = method.getName();
      this.parameterTypes = parameterTypes;
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    }
  }
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MethodKey)) {
      return false;
    }
    var other = (MethodKey) obj;
    return name == other.name &&
               equalParamTypes(parameterTypes,
                   other.parameterTypes);
  }
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
    return name.hashCode() + parameterTypes.length;
  }

  @Override
  public int compareTo(MethodKey that) {
    int result = this.name.compareTo(that.name);
    if (result != 0) return result;
    return Arrays.compare(this.parameterTypes,
        that.parameterTypes, Comparator.comparing(Class::getName));
  }

  @Override
  public String toString() {
    return Stream.of(parameterTypes)
               .map(Class::getName)
               .collect(Collectors.joining(", ",
                   name + "(", ")"));
  }
}
// end::listing[]

