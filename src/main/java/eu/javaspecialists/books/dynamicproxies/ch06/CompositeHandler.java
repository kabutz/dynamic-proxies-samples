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

package eu.javaspecialists.books.dynamicproxies.ch06;

import eu.javaspecialists.books.dynamicproxies.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

// tag::listing[]
public class CompositeHandler implements InvocationHandler {
  private final Map<MethodKey, Reducer> reducers;
  private final List<Object> children = new ArrayList<>();

  public <E extends Composite<? super E>> CompositeHandler(
      Class<? super E> target, Map<MethodKey, Reducer> reducers) {
    if (!Composite.class.isAssignableFrom(target))
      throw new IllegalArgumentException(
          "target is not derived from Composite");
    this.reducers = reducers == null ? Map.of() : reducers;
  }

  @Override
  public Object invoke(Object proxy,
                       Method method, Object[] args)
      throws Throwable {
    if (matches(method, "add")) {
      children.add(args[0]);
      return null;
    } else if (matches(method, "remove")) {
      return children.remove(args[0]);
    }
    class UncheckedException extends RuntimeException {
      public UncheckedException(Throwable cause) {
        super(cause);
      }
    }
    var reducer = reducers.getOrDefault(
        new MethodKey(method), Reducer.NULL_REDUCER);
    try {
      Function<Object, Object> mapFunction = child -> {
        try {
          return method.invoke(child, args);
        } catch (IllegalAccessException e) {
          throw new UncheckedException(e);
        } catch (InvocationTargetException e) {
          throw new UncheckedException(
              e.getCause());
        }
      };
      var result =
          children.stream()
              .map(mapFunction)
              .reduce(reducer.getIdentity(),
                  reducer.getMerger());
      if (reducer == Reducer.PROXY_INSTANCE_REDUCER)
        return proxy;
      return result;
    } catch (UncheckedException ex) {
      throw ex.getCause();
    }
  }

  private boolean matches(Method method, String name) {
    return name.equals(method.getName())
               && method.getParameterCount() == 1
               && method.getParameterTypes()[0]
                      == Object.class;
  }
}
// end::listing[]