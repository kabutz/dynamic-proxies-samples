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

package eu.javaspecialists.books.dynamicproxies.util.chain;

import java.lang.reflect.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A unified version of {@link VTableHandler} and {@link
 * VTableDefaultMethodsHandler}.  It works, but would probably
 * be more difficult to explain than the version with a bit of
 * duplicate code.
 *
 * @param <M> either a Method or MethodHandle
 */
public abstract class UnifiedInvocationHandler<M>
    extends ChainedInvocationHandler {
  @FunctionalInterface
  protected interface Invoker<M> {
    Object invoke(M match, Object receiver, Object[] args)
        throws Throwable;
  }

  private final Function<Method, M> lookup;
  private final Invoker<M> invoker;

  protected UnifiedInvocationHandler(
      Function<Method, M> lookup,
      Invoker<M> invoker,
      ChainedInvocationHandler next) {
    super(next);
    this.invoker = invoker;
    this.lookup = lookup;
  }

  @Override
  public final Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    M match = lookup.apply(method);
    if (match != null)
      return invoker.invoke(match, getReceiver(proxy), args);
    return super.invoke(proxy, method, args);
  }

  protected abstract Object getReceiver(Object proxy);

  @Override
  protected final Stream<Method> findUnhandledMethods(
      Class<?>... targets) {
    return super.findUnhandledMethods(targets)
               .filter(method -> lookup.apply(method) == null);
  }
}
