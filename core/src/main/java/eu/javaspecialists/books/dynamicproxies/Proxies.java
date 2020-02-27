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

package eu.javaspecialists.books.dynamicproxies;

import eu.javaspecialists.books.dynamicproxies.handlers.*;
import eu.javaspecialists.books.dynamicproxies.util.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;

/**
 * Facade for all our dynamic proxies and related pattern
 * implementations.
 */
public class Proxies {
  private Proxies() {}

  // tag::castProxy()[]
  /**
   * @param intf       The interface to implement and cast to
   * @param handler    InvocationHandler for all methods
   * @param extraIntfs Any additional interfaces that our dynamic
   *                   proxy should implement, e.g. Serializable
   */
  @SuppressWarnings("unchecked")
  public static <S> S castProxy(Class<? super S> intf,
                                InvocationHandler handler,
                                Class<?>... extraIntfs) {
    Objects.requireNonNull(intf, "intf==null");
    Objects.requireNonNull(handler, "handler==null");
    Objects.requireNonNull(extraIntfs, "extraIntfs==null");
    Class<?>[] intfs = new Class[extraIntfs.length + 1];
    intfs[0] = intf;
    System.arraycopy(extraIntfs, 0, intfs, 1, extraIntfs.length);
    return MethodTurboBooster.boost(
        (S) Proxy.newProxyInstance(
            intf.getClassLoader(),
            intfs,
            new ExceptionUnwrappingInvocationHandler(handler)));
  }
  // end::castProxy()[]

  // tag::simpleProxy()[]
  public static <S> S simpleProxy(
      Class<? super S> subjectInterface, S subject) {
    return castProxy(subjectInterface,
        (proxy, method, args) -> method.invoke(subject, args)
    );
  }
  // end::simpleProxy()[]

  // tag::loggingProxy()[]
  public static <S> S loggingProxy(
      Class<? super S> subjectInterface,
      S subject, Logger log) {
    Objects.requireNonNull(subject, "subject==null");
    return castProxy(subjectInterface,
        new LoggingInvocationHandler(log, subject));
  }
  // end::loggingProxy()[]

  // tag::virtualProxy()[]
  public static <S> S virtualProxy(
      Class<? super S> subjectInterface,
      Supplier<? extends S> subjectSupplier) {
    Objects.requireNonNull(subjectSupplier,
        "subjectSupplier==null");
    return castProxy(subjectInterface,
        new VirtualProxyHandler<>(subjectSupplier));
  }
  // end::virtualProxy()[]

  // tag::synchronizedProxy()[]
  public static <S> S synchronizedProxy(
      Class<? super S> subjectInterface, S subject) {
    Objects.requireNonNull(subject, "subject==null");
    return castProxy(subjectInterface,
        new SynchronizedHandler<>(subject));
  }
  // end::synchronizedProxy()[]

  // tag::filter()[]
  public static <F> F filter(
      Class<? super F> filter, Object component) {
    Objects.requireNonNull(component, "component==null");
    return castProxy(filter,
        new FilterHandler(filter, component));
  }
  // end::filter()[]

  // tag::adapt()[]
  public static <T> T adapt(Class<? super T> target,
                            Object adaptee,
                            Object adapter) {
    Objects.requireNonNull(adaptee, "adaptee==null");
    Objects.requireNonNull(adapter, "adapter==null");
    return castProxy(target,
        new ObjectAdapterHandler(target, adaptee, adapter));
  }
  // end::adapt()[]

  // tag::compose()[]
  public static <T extends BaseComponent<? super T>> T compose(
      Class<T> component) {
    return compose(component, component);
  }
  public static <T extends BaseComponent<? super T>> T compose(
      Class<T> component, Map<MethodKey, Reducer> reducers) {
    return compose(component, reducers, component);
  }
  public static <T extends BaseComponent<? super T>> T compose(
      Class<T> component, Class<?>... typeChecks) {
    return compose(component, Map.of(), typeChecks);
  }
  /**
   * @param component     interface to proxy. Must extend
   *                   BaseComponent
   * @param reducers   map from MethodKey to Reducer, default of
   *                   empty map with Map.of().
   * @param typeChecks object parameter passed to add() must
   *                   implement all these interfaces, default of
   *                   only the component interface.
   */
  public static <T extends BaseComponent<? super T>> T compose(
      Class<T> component, Map<MethodKey, Reducer> reducers,
      Class<?>... typeChecks) {
    // all objects that we add to the composite have to implement
    // all the interfaces in typeChecks
    return castProxy(component,
        new CompositeHandler(component, reducers, typeChecks));
  }
  // end::compose()[]
}