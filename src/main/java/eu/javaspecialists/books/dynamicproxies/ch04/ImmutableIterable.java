package eu.javaspecialists.books.dynamicproxies.ch04;

import java.util.Spliterator;
import java.util.function.Consumer;

// tag::ImmutableIterable[]
public interface ImmutableIterable<E> {
    void forEach(Consumer<? super E> action);
    Spliterator<E> spliterator();

    // mutators filtered away
    //    Iterator<E> iterator();
}
// end::ImmutableIterable[]

