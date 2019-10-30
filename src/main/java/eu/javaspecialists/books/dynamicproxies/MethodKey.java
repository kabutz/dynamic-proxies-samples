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

package eu.javaspecialists.books.dynamicproxies;

import java.lang.reflect.*;
import java.util.stream.*;

// tag::MethodKey[]
public final class MethodKey implements Comparable<MethodKey> {
   private final String id;
   public MethodKey(Method method) {
      this(method.getName(), method.getParameterTypes());
   }
   public MethodKey(String name, Class<?>... parameterTypes) {
      id = Stream.of(parameterTypes)
                 .map(Class::getName)
                 .collect(Collectors.joining(", ",
                       name + "(", ")"));
   }
   @Override
   public boolean equals(Object o) {
      if (!(o instanceof MethodKey)) return false;
      return id.equals(((MethodKey) o).id);
   }
   @Override
   public int hashCode() {
      return id.hashCode();
   }
   @Override
   public int compareTo(MethodKey mk) {
      return id.compareTo(mk.id);
   }
   @Override
   public String toString() {
      return id;
   }
}
// end::MethodKey[]

