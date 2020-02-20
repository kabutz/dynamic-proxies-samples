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

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

// tag::listing[]
public class CompositeHandler
    implements InvocationHandler {
  private final Map<MethodKey, Reducer> reducers;
  private final Class<?>[] typeChecks;
  private final List<Object> children = new ArrayList<>();
  private final VTable defaultVT;
  private final Map<Class<?>, VTable> childMethodMap =
      new ConcurrentHashMap<>();
  private final Class<?> target;

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <E extends BaseComponent<? super E>> CompositeHandler(
      Class<? super E> target,
      Map<MethodKey, Reducer> reducers,
      Class<?>[] typeChecks) {
    if (!BaseComponent.class.isAssignableFrom(target))
      throw new IllegalArgumentException(
          "target is not derived from BaseComponent");
    this.target = target;
    this.reducers = Objects.requireNonNull(reducers);
    this.typeChecks = Objects.requireNonNull(typeChecks);
    this.defaultVT = VTables.newDefaultMethodVTable(target);
  }

  @Override
  public Object invoke(Object proxy,
                       Method method, Object[] args)
      throws Throwable {
    // Look for "add(Object)" and "remove(Object)" methods
    // from BaseComponent
    if (matches(method, "add")) {
      requiresAllInterfaces(args[0]);
      // We need the childMethodMap to support the visitor
      // pattern inside our composite structures
      childMethodMap.computeIfAbsent(args[0].getClass(),
          childClass -> {
            Module childModule = childClass.getModule();
            Module targetModule = target.getModule();
            Class<?> receiverClass;
            if (childModule.isExported(
                childClass.getPackageName(), targetModule)) {
              // only map child class methods if it is visible to
              // the target module
              receiverClass = childClass;
            } else if (Proxy.class.isAssignableFrom(childClass)){
              // childClass is a Proxy, use the first interface
              receiverClass = childClass.getInterfaces()[0];
            } else {
              receiverClass = target;
            }
            return VTables.newVTableExcludingObjectMethods(
                receiverClass, target);
          });
      return children.add(args[0]);
    } else if (matches(method, "remove")) {
      return children.remove(args[0]);
    }

    /**
     * This special class defined inside the method is only
     * visible inside the method. It is used to wrap and later
     * unwrap checked exceptions. We override fillInStackTrace()
     * to return null, so that we do not incur the cost of an
     * additional stack trace.
     */
    class UncheckedException extends RuntimeException {
      public UncheckedException(Throwable cause) {
        super(cause);
      }
      @Override
      public Throwable fillInStackTrace() { return null; }
    }

    // The purpose of the mapFunction is to convert checked
    // exceptions from our call to method.invoke() into
    // an UncheckedException, which we will unwrap later.
    // Unlike the reducers, we need to create a new lambda
    // each time we call the invoke() method, as we need to
    // capture the method and args parameters.
    Function<Object, Object> mapFunction = child -> {
      try {
        VTable vt = childMethodMap.get(child.getClass());
        Objects.requireNonNull(vt, "vt==null");
        Method childMethod = vt.lookup(method);
        Objects.requireNonNull(childMethod, "childMethod==null");
        return childMethod.invoke(child, args);
      } catch (IllegalAccessException e) {
        throw new UncheckedException(e);
      } catch (InvocationTargetException e) {
        throw new UncheckedException(e.getCause());
      }
    };

    // The reducer is used to "reduce" results from method calls
    // to a single result.  By default we will use the
    // NULL_REDUCER, which always returns null.  This is
    // suitable for methods that return void.
    var reducer = reducers.getOrDefault(
        new MethodKey(method), Reducer.NULL_REDUCER);
    try {
      MethodHandle match = defaultVT.lookupDefaultMethod(method);
      Object defaultMethodResult;
      if (match == null) {
        defaultMethodResult = reducer.getIdentity();
      } else {
        defaultMethodResult =
            match.bindTo(proxy).invokeWithArguments(args);
      }

      // We now need to call the method on all our children and
      // do a "reduce" on the results to return a single result.
      var merger = reducer.getMerger();
      var result = children.stream()
                       .map(mapFunction)
                       .reduce(reducer.getIdentity(), merger);
      // A special case of reducer is PROXY_INSTANCE_REDUCER.
      // When that is specified, we return the proxy instance
      // instead.  This is useful to support fluent interfaces
      // that return "this".
      if (reducer == Reducer.PROXY_INSTANCE_REDUCER)
        return proxy;
      else
        return merger.apply(result, defaultMethodResult);
    } catch (UncheckedException ex) {
      // Lastly we unwrap the UncheckedException and throw the
      // cause.
      throw ex.getCause();
    }
  }

  /**
   * Specific match for add(Object) and remove(Object) methods.
   */
  private boolean matches(Method method, String name) {
    return name.equals(method.getName())
               && method.getParameterCount() == 1
               && ParameterTypesFetcher.get(method)[0]
                      == Object.class;
  }

  private void requiresAllInterfaces(Object arg) {
    for (var check : typeChecks) {
      if (!check.isInstance(arg))
        throw new ClassCastException(
            arg.getClass() + " cannot be cast to " + check);
    }
  }
}
// end::listing[]