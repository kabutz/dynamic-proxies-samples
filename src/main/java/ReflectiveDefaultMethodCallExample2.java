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

import java.lang.invoke.*;
import java.lang.reflect.*;

public class ReflectiveDefaultMethodCallExample2 {


  private interface DefaultMethodMarker {}

  public static void main(String[] args) throws Throwable {
    DefaultMethods target =
        //new Hello(){};
        (DefaultMethods) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
            new Class[] {DefaultMethods.class, DefaultMethodMarker.class},
            (proxy, method, arguments) -> null);
    System.out.println(target.getClass());
    System.out.println(Modifier.isPublic(target.getClass().getModifiers()));

    for (Method method :
        target.getClass().getDeclaredMethods()) {
      System.out.println("method = " + method);

    }
    Method method = DefaultMethods.class.getMethod("bar");

    System.out.println("method.getDeclaringClass() = " + method.getDeclaringClass());

    Object result = MethodHandles.lookup()
                        .in(method.getDeclaringClass())
                        .unreflectSpecial(method,
                            method.getDeclaringClass())
                        .bindTo(target)
                        .invokeWithArguments();
    System.out.println(result); //Hello


    MethodHandles.lookup()
        .unreflectSpecial(method, DefaultMethods.class);

    MethodHandles.privateLookupIn(DefaultMethods.class,
        MethodHandles.lookup())
        .in(method.getDeclaringClass())
        .unreflectSpecial(method, method.getDeclaringClass())
        .bindTo(target)
        .invokeWithArguments();
  }
}
