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

import eu.javaspecialists.books.dynamicproxies.*;

import java.lang.invoke.*;
import java.lang.reflect.*;

public class DefaultMethodsTest {
  public static void main(String... args) {

    Class<?> target = DefaultMethods.class;

    DefaultMethods dm = Proxies.castProxy(target,
        (proxy, method, args1) -> {
          System.out.println(method.isDefault());
          System.out.println(method);

          if (method.isDefault()) {
            Object obj  =
                 Proxy.newProxyInstance(target.getClassLoader(),
                    new Class[] {target}, (Object p, Method m, Object[] a) -> null);
            return MethodHandles.privateLookupIn(target, MethodHandles.lookup())
                                .in(method.getDeclaringClass())
                                .unreflectSpecial(method,
                                    method.getDeclaringClass())
                                .bindTo(proxy)
                                .invokeWithArguments();
          }
          return null;
        });

    dm.foo();
    dm.bar();
    DefaultMethods.baz();
  }
}
