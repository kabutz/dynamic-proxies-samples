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

package eu.javaspecialists.books.dynamicproxies.samples.ch03.finalize;

import java.lang.reflect.*;
import java.util.*;

public class FinalizeExample {
  @SuppressWarnings("unchecked")
  public static void main(String... args) {
    List<String> list = (List<String>) Proxy.newProxyInstance(
        FinalizeExample.class.getClassLoader(),
        new Class[] {List.class, FinalizingProxy.class},
        new InvocationHandler() {
          private final List<String> origin =
              List.of("John", "Anton", "Heinz");
          @Override
          public Object invoke(Object proxy, Method method,
                               Object[] args) throws Throwable {
            System.out.println("method = " + method);
            if (method.getName() == "finalize") {
              System.out.println("Not delegating to origin");
              return null;
            } else return method.invoke(origin, args);
          }
        }
    );

    System.out.println("list = " + list);
    list.stream().map(String::toUpperCase)
        .forEach(System.out::println);
    list = null;
    for (int i = 0; i < 3; i++) {
      System.gc();
    }
  }

  /*
  Output:
method = public java.lang.String java.lang.Object.toString()
list = [John, Anton, Heinz]
method = public default java.util.stream.Stream java.util.Collection.stream()
JOHN
ANTON
HEINZ
method = public abstract void eu.javaspecialists.books.dynamicproxies.samples.ch03.finalize.FinalizingProxy.finalize()
Not delegating to origin
   */
}
