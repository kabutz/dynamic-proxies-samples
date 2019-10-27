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

package eu.javaspecialists.books.dynamicproxies.ch03.scratch;

import java.lang.reflect.*;

public class Hackity {
    public static void main(String... args) throws ClassNotFoundException {
        Class<?> intf = Class.forName("java.util.stream.Sink");
        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method,
                                 Object[] args) throws Throwable {
                System.out.println("Hackity.invoke");

                return null;
            }
        };
        Object proxy = Proxy.newProxyInstance(intf.getClassLoader(),
                new Class<?>[] {intf},
                h);
        System.out.println(proxy.getClass());
        System.out.println(h.getClass().getClassLoader());
    }
}
