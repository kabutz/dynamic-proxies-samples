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

import eu.javaspecialists.books.dynamicproxies.ch03.*;
import eu.javaspecialists.books.dynamicproxies.ch04.*;
import eu.javaspecialists.books.dynamicproxies.ch05.*;
import eu.javaspecialists.books.dynamicproxies.ch06.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;

/**
 * Facade for all our dynamic proxies and related pattern implementations.
 */
public class Proxies {
    private Proxies() {}

    // tag::simpleProxy()[]
    public static <P> P simpleProxy(Class<P> proxiedInterface, P p) {
        return castProxy(proxiedInterface,
                (proxy, method, args) -> method.invoke(p, args)
        );
    }
    public static <P> P castProxy(Class<P> proxiedInterface,
                                  InvocationHandler handler) {
        return proxiedInterface.cast(Proxy.newProxyInstance(
                proxiedInterface.getClassLoader(),
                new Class<?>[] {proxiedInterface}, handler
        ));
    }
    // end::simpleProxy()[]

    // tag::loggingProxy()[]
    public static <P> P loggingProxy(Class<P> proxiedInterface,
                                     P p, Logger log) {
        return castProxy(proxiedInterface,
                new LoggingInvocationHandler(log, p));
    }
    // end::loggingProxy()[]

    // tag::virtualProxy()[]
    public static <P> P virtualProxy(Class<P> proxiedInterface,
                                     Supplier<? extends P> supplier) {
        return castProxy(proxiedInterface,
                new VirtualProxyHandler<P>(supplier));
    }
    // end::virtualProxy()[]

    // tag::dynamicFilter()[]
    public static <P> P dynamicFilter(Class<P> filter, Object component) {
        return castProxy(filter, new FilterHandler(filter, component));
    }
    // end::dynamicFilter()[]

    // tag::adapt()[]
    public static <E> E adapt(Object adaptee, Class<E> target,
                              Object adapter) {
        return castProxy(target,
                new ObjectAdapterHandler(adapter, adaptee));
    }
    // end::adapt()[]

    // tag::compose()[]
    public static <E extends Composite<E>> E compose(Class<E> target) {
        return compose(target, null);
    }
    public static <E extends Composite<E>> E compose(
            Class<E> target, Map<MethodKey, Reducer> mergers) {
        return castProxy(target,
                new CompositeHandler(target, mergers));
    }
    // end::compose()[]

}
