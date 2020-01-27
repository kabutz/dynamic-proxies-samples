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

import java.lang.invoke.*;
import java.lang.reflect.*;

// tag::listing[]
/**
 * Fast fetching of parameter types array is disabled by default
 * and can be enabled with
 * -Deu.javaspecialists.books.dynamicproxies.util. \
 * ParameterTypesFetcher.enabled=true
 */
public class ParameterTypesFetcher {
  private final static ParameterFetcher PARAMETER_FETCHER =
      Boolean.getBoolean(
          ParameterTypesFetcher.class.getName() + ".enabled") ?
          new FastParameterFetcher() :
          new NormalParameterFetcher();

  /**
   * Warning: When "fast parameter fetching" is enabled, the
   * array returned is the actual array stored inside the Method
   * object.  Do not change it!
   */
  public static Class<?>[] get(Method method) {
    return PARAMETER_FETCHER.getParameterTypes(method);
  }

  @FunctionalInterface
  private interface ParameterFetcher {
    Class<?>[] getParameterTypes(Method method);
  }

  /**
   * Returns the parameter types by calling getParameterTypes()
   * on the method parameter.  This should always work.
   */
  private static class NormalParameterFetcher
      implements ParameterFetcher {
    @Override
    public Class<?>[] getParameterTypes(Method method) {
      return method.getParameterTypes(); // clones the array
    }
  }

  /**
   * Creates a VarHandle pointing directly to the private field
   * parameterTypes stored inside Method. Since the VarHandle is
   * declared as final static, the cost of reading it is the
   * same as reading an ordinary field.
   */
  private static class FastParameterFetcher
      implements ParameterFetcher {
    private final static VarHandle METHOD_PARAMETER_TYPES;

    static {
      try {
        METHOD_PARAMETER_TYPES = MethodHandles.privateLookupIn(
            Method.class, MethodHandles.lookup()
        ).findVarHandle(Method.class, "parameterTypes",
            Class[].class);
      } catch (ReflectiveOperationException e) {
        throw new Error(e);
      }
    }
    @Override
    public Class<?>[] getParameterTypes(Method method) {
      return (Class<?>[]) METHOD_PARAMETER_TYPES.get(method);
    }
  }
}
// end::listing[]