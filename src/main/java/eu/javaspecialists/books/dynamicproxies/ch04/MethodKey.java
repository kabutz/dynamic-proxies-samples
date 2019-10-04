package eu.javaspecialists.books.dynamicproxies.ch04;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// tag::MethodKey[]
final class MethodKey implements Comparable<MethodKey> {
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
// end::MethodKey[]

