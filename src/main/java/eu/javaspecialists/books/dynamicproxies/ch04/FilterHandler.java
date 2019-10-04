package eu.javaspecialists.books.dynamicproxies.ch04;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// tag::FilterHandler[]
class FilterHandler implements InvocationHandler {
    private final Map<MethodKey, Method> methodMap;
    private final Object component;

    public FilterHandler(Class<?> filter, Object component) {
        this.component = component;
        Map<MethodKey, Method> reverse = createReverseMap(component);

        Map<MethodKey, Method> methodMap = new HashMap<>();
        for (Method filterMethod : filter.getMethods()) {
            MethodKey key = new MethodKey(filterMethod);
            Method componentMethod = reverse.get(key);
            if (componentMethod == null)
                throw new IllegalArgumentException(
                    "No matching method for filter method " + key);
            methodMap.put(new MethodKey(filterMethod), componentMethod);
        }

        for (Method objectMethod : Object.class.getMethods()) {
            MethodKey key = new MethodKey(objectMethod);
            Method componentMethod = reverse.get(key);
            if (componentMethod == null)
                throw new AssertionError( // cannot happen :-)
                    "No matching method for object method " + key);
            methodMap.put(new MethodKey(objectMethod), componentMethod);
        }
        this.methodMap = Map.copyOf(methodMap); // @since Java 10
    }
    /**
     * Our reverse map allows us to find the methods in the component
     * that we are decorating.
     */
    private static Map<MethodKey, Method> createReverseMap(Object o) {
        Map<MethodKey, Method> reverse = new HashMap<>();
        addReverseMethods(o.getClass(), reverse);
        return reverse;
    }
    /**
     * Recursively add all "truly" public methods from this class,
     * superclasses and interfaces.  By "truly" we mean methods that are
     * public and which are defined inside public classes.
     */
    private static void addReverseMethods(Class<?> clazz,
                                          Map<MethodKey, Method> reverse) {
        if (clazz == null) return;
        for (Method method : clazz.getMethods()) {
            if (isTrulyPublic(method)) {
                reverse.putIfAbsent(new MethodKey(method), method);
            }
        }
        for (Class<?> anInterface : clazz.getInterfaces()) {
            addReverseMethods(anInterface, reverse);
        }
        addReverseMethods(clazz.getSuperclass(), reverse);
    }

    /**
     * Truly public are those methods where the declaring class is also
     * public, hence the bitwise AND.
     */
    private static boolean isTrulyPublic(Method method) {
        return Modifier.isPublic(method.getModifiers()
            & method.getDeclaringClass().getModifiers());
    }

    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        MethodKey key = new MethodKey(method);
        return methodMap.get(key).invoke(component, args);
    }

    private static class MethodKey implements Comparable<MethodKey> {
        private final Method method;
        private final String id;
        public MethodKey(Method method) {
            this.method = method;
            id = Stream.of(method.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.joining(", ",
                    method.getName() + "(", ")"));
        }
        public boolean equals(Object o) {
            if (!(o instanceof MethodKey)) return false;
            return id.equals(((MethodKey) o).id);
        }
        public int hashCode() {
            return id.hashCode();
        }
        public int compareTo(MethodKey mk) {
            return id.compareTo(mk.id);
        }
        public String toString() {
            return id;
        }
    }
}
// end::FilterHandler[]

