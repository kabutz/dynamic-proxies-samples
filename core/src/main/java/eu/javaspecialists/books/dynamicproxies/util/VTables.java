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

// tag::listing[]
public final class VTables {
  private VTables() {}

  public static VTable newDefaultMethodVTable(Class<?> clazz) {
    return newVTableBuilder(clazz, clazz)
               .excludeObjectMethods()
               .includeDefaultMethods()
               .build();
  }

  public static VTable newVTable(Class<?> receiver,
                                 Class<?>... targets) {
    return newVTableBuilder(receiver, targets).build();
  }

  public static VTable newVTableExcludingObjectMethods(
      Class<?> receiver, Class<?>... targets) {
    return newVTableBuilder(receiver, targets)
               .excludeObjectMethods()
               .build();
  }

  private static VTable.Builder newVTableBuilder(
      Class<?> receiver, Class<?>... targets) {
    VTable.Builder builder = new VTable.Builder(receiver);
    for (Class<?> target : targets) {
      builder.addTargetInterface(target);
    }
    return builder;
  }
}
// end::listing[]