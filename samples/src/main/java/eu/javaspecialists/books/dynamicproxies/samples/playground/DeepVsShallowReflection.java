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

package eu.javaspecialists.books.dynamicproxies.samples.playground;

import java.lang.reflect.*;
import java.util.*;

public class DeepVsShallowReflection {
  public static void main(String[] args)
      throws ReflectiveOperationException {
    System.setSecurityManager(new SecurityManager());
    // Shallow Reflection
    Object o = "Hello world";
    Method toString = Object.class.getMethod("toString");
    System.out.println(toString.invoke(o));
    Method toUpperCase = o.getClass().getMethod("toUpperCase");
    System.out.println(toUpperCase.invoke(o));

    List<String> list = Arrays.asList("Hello", "World");
    System.out.println(list.size());
    Method size = List.class.getMethod("size");
    System.out.println(size.invoke(list));

    System.out.println("End of shallow reflection");

    // Deep Reflection
    Field value = String.class.getDeclaredField("value");
    System.out.println("value = " + value);
    value.setAccessible(true);
    value.set(o, "Goodbye, cruel world".getBytes());
    System.out.println(o);

    Constructor<String> str = String.class.getDeclaredConstructor(byte[].class, byte.class);
    str.setAccessible(true);
    byte[] bytes = "Hello Safari".getBytes();
    String s = str.newInstance(bytes, (byte)0);
    System.out.println(s);
    bytes[0] = 'J';
    System.out.println(s);
  }
}
