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

package eu.javaspecialists.books.dynamicproxies.ch03.gotchas;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.util.*;

import java.lang.reflect.*;
import java.math.*;
import java.util.function.*;

// tag::listing[]
/**
 * Ridiculous impractical example showing recursive dynamic proxy
 * calls.  Please do not code like this!
 */
public class RecursiveDynamicProxy {
  public static void main(String... args) {
    IntFunction<BigInteger> factorial =
        Proxies.castProxy(IntFunction.class,
            new Factorial());
    System.out.println(factorial.apply(5));
  }

  private static class Factorial implements InvocationHandler {
    private final static MethodKey apply =
        new MethodKey(IntFunction.class, "apply", int.class);
    @Override
    public Object invoke(Object proxy,
                         Method method,
                         Object[] params) throws Throwable {
      if (new MethodKey(method).equals(apply)) {
        int n = (int) params[0];
        if (n == 0) {
          Thread.dumpStack();
          return BigInteger.ONE;
        }
        BigInteger other = (BigInteger) method.invoke(
            proxy, n - 1);
        return BigInteger.valueOf(n).multiply(other);
      } else {
        throw new UnsupportedOperationException(
            "only apply(int) supported");
      }
    }
  }
}
// end::listing[]
