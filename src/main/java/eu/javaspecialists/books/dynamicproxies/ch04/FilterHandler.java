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

package eu.javaspecialists.books.dynamicproxies.ch04;

import eu.javaspecialists.books.dynamicproxies.*;

import java.lang.reflect.*;
import java.util.*;

// tag::FilterHandler[]
public class FilterHandler implements InvocationHandler {
   private final Map<MethodKey, Method> methodMap;
   private final Object component;

   public FilterHandler(Class<?> filter, Object component) {
      this.component = component;
      Map<MethodKey, Method> reverse = createReverseMap(component);

      Map<MethodKey, Method> methodMap = new HashMap<>();
      for (Method filterMethod : filter.getMethods()) {
         MethodKey key = new MethodKey(filterMethod);
         Method componentMethod = reverse.get(key);
         if (componentMethod == null)
            throw new IllegalArgumentException(
                  "No matching method for filter method " + key);
         methodMap.put(new MethodKey(filterMethod), componentMethod);
      }

      for (Method objectMethod : Object.class.getMethods()) {
         MethodKey key = new MethodKey(objectMethod);
         Method componentMethod = reverse.get(key);
         if (componentMethod == null)
            throw new AssertionError( // cannot happen :-)
                  "No matching method for object method " + key);
         methodMap.put(new MethodKey(objectMethod), componentMethod);
      }
      this.methodMap = Map.copyOf(methodMap); // @since Java 10
   }
   /**
    * Our reverse map allows us to find the methods in the component
    * that we are decorating.
    */
   private static Map<MethodKey, Method> createReverseMap(Object o) {
      Map<MethodKey, Method> reverse = new HashMap<>();
      addReverseMethods(o.getClass(), reverse);
      return reverse;
   }
   /**
    * Recursively add all "truly" public methods from this class,
    * superclasses and interfaces.  By "truly" we mean methods that are
    * public and which are defined inside public classes.
    */
   private static void addReverseMethods(Class<?> clazz,
                                         Map<MethodKey, Method> reverse) {
      if (clazz == null) return;
      for (Method method : clazz.getMethods()) {
         if (isTrulyPublic(method)) {
            reverse.putIfAbsent(new MethodKey(method), method);
         }
      }
      for (Class<?> anInterface : clazz.getInterfaces()) {
         addReverseMethods(anInterface, reverse);
      }
      addReverseMethods(clazz.getSuperclass(), reverse);
   }

   /**
    * Truly public are those methods where the declaring class is also
    * public, hence the bitwise AND.
    */
   private static boolean isTrulyPublic(Method method) {
      return Modifier.isPublic(method.getModifiers()
                                     & method.getDeclaringClass().getModifiers());
   }

   @Override
   public Object invoke(Object proxy,
                        Method method,
                        Object[] args) throws Throwable {
      MethodKey key = new MethodKey(method);
      return methodMap.get(key).invoke(component, args);
   }
}
// end::FilterHandler[]

