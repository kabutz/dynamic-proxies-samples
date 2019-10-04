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

package eu.javaspecialists.books.dynamicproxies.ch03;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class LoggingProxy {
    public static <P> P makeLoggingProxy(Class<P> clazz, P p, Logger log) {
        return clazz.cast(Proxy.newProxyInstance(
            clazz.getClassLoader(),
            new Class<?>[]{clazz},
            new LoggingInvocationHandler(log, p)
        ));
    }
    public static void main(String... args) {
        // tag::main()[]
        InvocationHandler handler = new LoggingInvocationHandler(
            Logger.getGlobal(),
            new ConcurrentHashMap<>());
        Map<String, Integer> map = (Map<String, Integer>)
            Proxy.newProxyInstance(
                Map.class.getClassLoader(),
                new Class<?>[]{Map.class},
                handler);
        map.put("one", 1);
        map.put("two", 2);
        System.out.println(map);
        // end::main()[]
    }

    static
    // tag::LoggingInvocationHandler[]
    public class LoggingInvocationHandler implements InvocationHandler {
        private final Logger log;
        private final Object o;
        public LoggingInvocationHandler(Logger log, Object o) {
            this.log = log;
            this.o = o;
        }
        public Object invoke(Object proxy,
                             Method method,
                             Object[] args) throws Throwable {
            log.info(() -> "Entering " + method + " with parameters " +
                Arrays.toString(args));
            long start = System.nanoTime();
            try {
                return method.invoke(o, args);
            } finally {
                long nanos = System.nanoTime() - start;
                log.info(() -> "Exiting " + method + " with parameters " +
                    Arrays.toString(args) + " took " + nanos + "ns");
            }
        }
    }
    // end::LoggingInvocationHandler[]
}
