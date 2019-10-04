package eu.javaspecialists.books.dynamicproxies.ch04;

import java.util.Collection;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

// tag::HandcodedFilter[]
public class HandcodedFilter<E> implements ImmutableCollection<E> {
    private final Collection<E> c;

    public HandcodedFilter(Collection<E> c) {
        this.c = c;
    }

    public int size() {
        return c.size();
    }
    public boolean isEmpty() {
        return c.isEmpty();
    }
    public boolean contains(Object o) {
        return c.contains(o);
    }
    public Object[] toArray() {
        return c.toArray();
    }
    public <T> T[] toArray(T[] a) {
        return c.toArray(a);
    }
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return c.toArray(generator);
    }
    public boolean containsAll(Collection<?> c) {
        return this.c.containsAll(c);
    }
    public Spliterator<E> spliterator() {
        return c.spliterator();
    }
    public Stream<E> stream() {
        return c.stream();
    }
    public Stream<E> parallelStream() {
        return c.parallelStream();
    }
    public void forEach(Consumer<? super E> action) {
        c.forEach(action);
    }
}
// end::HandcodedFilter[]

