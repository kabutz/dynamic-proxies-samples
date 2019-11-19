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

package eu.javaspecialists.books.dynamicproxies.ch05;

import eu.javaspecialists.books.dynamicproxies.util.*;
import eu.javaspecialists.books.dynamicproxies.util.chain.*;

import java.lang.reflect.*;

// tag::listing[]
public class ObjectAdapterHandler implements InvocationHandler {
  private final ChainedInvocationHandler chain;

  public ObjectAdapterHandler(Class<?> target,
                              Object adaptee,
                              Object adapter) {
    checkClassPublic(adaptee.getClass());
    checkClassPublic(adapter.getClass());

    VTable adapterVT = VTables.newVTable(
        adapter.getClass(), target);
    VTable adapteeVT = VTables.newVTable(
        adaptee.getClass(), target);
    VTable defaultVT = VTables.newDefaultMethodVTable(target);

    chain = new VTableHandler(adapter, adapterVT,
        new VTableHandler(adaptee, adapteeVT,
            new VTableDefaultMethodsHandler(defaultVT, null)));

    chain.checkAllMethodsAreHandled(target);
  }

  @Override
  public Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    try {
      return chain.invoke(proxy, method, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void checkClassPublic(Class<?> clazz) {
    if (!Modifier.isPublic(clazz.getModifiers()))
      throw new IllegalArgumentException(
          clazz + " needs to be public");
  }
}
// end::listing[]