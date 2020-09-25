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

package eu.javaspecialists.books.dynamicproxies.handlers;

import eu.javaspecialists.books.dynamicproxies.util.*;
import eu.javaspecialists.books.dynamicproxies.util.chain.*;

import java.lang.reflect.*;

// tag::listing[]
public final class FilterHandler implements InvocationHandler {
  private final ChainedInvocationHandler chain;

  public FilterHandler(Class<?> filter, Object component) {
    VTable vt = VTables.newVTable(component.getClass(), filter);
    VTable defaultVT = VTables.newDefaultMethodVTable(filter);

    chain = new VTableHandler(component, vt,
        new VTableDefaultMethodsHandler(defaultVT, null));

    chain.checkAllMethodsAreHandled(filter);
  }

  @Override
  public Object invoke(Object proxy,
                       Method method,
                       Object[] args) throws Throwable {
    return chain.invoke(proxy, method, args);
  }
}
// end::listing[]