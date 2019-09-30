package eu.javaspecialists.books.dynamicproxies.ch03;

import java.lang.reflect.*;
import java.util.*;

// tag::BrokenReflection[]
public class BrokenReflection {
    public static void main(String... args)
            throws ReflectiveOperationException {
        Collection<String> my_world = Arrays.asList(
                "hello", "world");
        Method size = my_world.getClass().getMethod("size");
        System.out.println(size.invoke(my_world));
    }
}
// end::BrokenReflection[]

