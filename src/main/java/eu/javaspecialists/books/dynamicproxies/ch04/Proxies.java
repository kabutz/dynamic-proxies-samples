package eu.javaspecialists.books.dynamicproxies.ch04;

import java.lang.reflect.Proxy;

// tag::Proxies[]
public class Proxies {
    private Proxies() {}
    public static <P> P createDynamicFilter(
        Class<P> filter, Object component
    ) {
        return filter.cast(
            Proxy.newProxyInstance(
                filter.getClassLoader(),
                new Class<?>[]{filter},
                new FilterHandler(filter, component)
            )
        );
    }
}
// end::Proxies[]


