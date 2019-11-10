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

package eu.javaspecialists.books.dynamicproxies.ch03.virtual;

import java.lang.reflect.*;
import java.util.function.*;

// tag::listing[]
public class VirtualProxyHandler<E> implements InvocationHandler {
  private final Supplier<? extends E> supplier;
  private E e;

  public VirtualProxyHandler(Supplier<? extends E> supplier) {
    this.supplier = supplier;
  }
  private E getE() {
    if (e == null) e = supplier.get();
    return e;
  }
  @Override
  public Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    return method.invoke(getE(), args);
  }
}
// end::listing[]