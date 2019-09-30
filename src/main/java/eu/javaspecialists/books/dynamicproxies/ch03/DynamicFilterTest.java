package eu.javaspecialists.books.dynamicproxies.ch03;

import java.util.Arrays;

public class DynamicFilterTest {
    public static void main(String... args) {
        ImmutableCollection<String> names =
            Proxies.createDynamicFilter(
                ImmutableCollection.class,
                Arrays.asList("Peter", "Paul", "Mary")
            );
        // names.remove("Peter"); // does not compile
        System.out.println(names);
        System.out.println("Do we have Mary? " + names.contains("Mary"));
        System.out.println("Are there names? " + names.isEmpty());
        System.out.println("Printing the names:");
        names.forEach(System.out::println);
        System.out.println("Class: " + names.getClass());
    }
}
