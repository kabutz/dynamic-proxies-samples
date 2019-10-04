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
