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

// tag::listing[]
/**
 * Method turbo boosting is enabled by default.  We call
 * setAccessible(true) on Method objects. Exceptions are silently
 * ignored.
 * <p>
 * Method turbo boosting can be disabled with
 * -Deu.javaspecialists.books.dynamicproxies.util.\
 * MethodTurboBooster.disabled=true
 */
public class MethodTurboBooster {
  private final static Booster BOOSTER =
      Boolean.getBoolean(
          MethodTurboBooster.class.getName() + ".disabled") ?
          new BoosterOff() : new BoosterOn();

  public static <E> E boost(E proxy) {
    return BOOSTER.turboBoost(proxy);
  }

  public static Method boost(Method method) {
    return BOOSTER.turboBoost(method);
  }

  private interface Booster {
    <E> E turboBoost(E proxy);
    Method turboBoost(Method method);
  }

  private static class BoosterOn implements Booster {
    @Override
    public <E> E turboBoost(E proxy) {
      if (!(proxy instanceof Proxy))
        throw new IllegalArgumentException(
            "Can only turboboost instances of Proxy"
        );
      try {
        for (var field : proxy.getClass().getDeclaredFields()) {
          if (field.getType() == Method.class) {
            field.setAccessible(true);
            turboBoost((Method) field.get(null));
          }
        }
        return proxy;
      } catch (IllegalAccessException | RuntimeException e) {
        // could not turbo-boost - return proxy unchanged;
        return proxy;
      }
    }
    @Override
    public Method turboBoost(Method method) {
      try {
        method.setAccessible(true);
      } catch (RuntimeException e) {
        // could not turbo-boost - return method unchanged;
      }
      return method;
    }
  }

  private static class BoosterOff implements Booster {
    @Override
    public <E> E turboBoost(E proxy) {
      if (!(proxy instanceof Proxy))
        throw new IllegalArgumentException(
            "Can only turboboost instances of Proxy"
        );
      return proxy;
    }
    @Override
    public Method turboBoost(Method method) {
      return method;
    }
  }
}
// end::listing[]