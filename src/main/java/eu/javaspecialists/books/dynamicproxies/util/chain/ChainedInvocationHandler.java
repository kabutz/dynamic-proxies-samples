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

package eu.javaspecialists.books.dynamicproxies.util.chain;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// tag::listing[]
/**
 * Chain of responsibility design pattern for invocation
 * handlers.   The invoke method by default passes the call to
 * the next element in the chain.  If no one can handle the
 * call, then that is an AssertionError.  Once the full chain
 * is constructed, we should check that all methods of the
 * target interface can be handled by our chain by calling
 * #checkAllMethodsAreHandled(targets).
 */
public abstract class ChainedInvocationHandler
    implements InvocationHandler {
  private final ChainedInvocationHandler next;

  public ChainedInvocationHandler(
      ChainedInvocationHandler next) {
    this.next = next;
  }

  @Override
  public Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    if (next != null) return next.invoke(proxy, method, args);
    // we cannot allow a method to not be handled
    throw new AssertionError(
        "No InvocationHandler for " + method);
  }

  /**
   * Should be called on the first link of the chain once
   * everything has been set up. Throws IllegalArgumentException
   * if any methods are not handled by our chain.
   *
   * @throws UnhandledMethodException if any method in the
   *                                  targets is not handled
   *                                  by the chain
   * @throws IllegalArgumentException if one of the targets is
   *                                  not an interface
   */
  public void checkAllMethodsAreHandled(Class<?>... targets) {
        if (Stream.of(targets)
            .anyMatch(Predicate.not(Class::isInterface)))
          throw new IllegalArgumentException(
              "target classes must be interfaces");
    Collection<Method> unhandled =
        findUnhandledMethods(targets)
            .collect(Collectors.toList());
    if (!unhandled.isEmpty())
      throw new UnhandledMethodException(unhandled);
  }

  /**
   * Last handler in the chain returns a Stream containing all
   * the methods in the given target interfaces.  Subclasses
   * should call super.findUnhandledMethods(targets) and then
   * add filters to remove methods that are handled by their
   * handlers.
   */
  protected Stream<Method> findUnhandledMethods(
      Class<?>... targets) {
    if (next != null) return next.findUnhandledMethods(targets);
    return Stream.of(targets)
               .map(Class::getMethods)
               .flatMap(Stream::of)
               .filter(
                   m -> !Modifier.isStatic(m.getModifiers()));
  }
}
// end::listing[]