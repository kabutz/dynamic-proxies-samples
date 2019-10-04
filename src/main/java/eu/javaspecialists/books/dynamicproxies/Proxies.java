package eu.javaspecialists.books.dynamicproxies;

import eu.javaspecialists.books.dynamicproxies.ch03.Listing2_1;

/**
 * Facade for all our dynamic proxies and related pattern implementations.
 */
public class Proxies {
    private Proxies() {}
    public static <P> P makeSimpleProxy(Class<P> clazz, P p) {
        return Listing2_1.Proxies.makeSimpleProxy(clazz, p);
    }

    public static <P> P createDynamicFilter(Class<P> filter,
                                            Object component) {
        return eu.javaspecialists.books.dynamicproxies.ch04.Proxies.createDynamicFilter(filter, component);
    }

}
