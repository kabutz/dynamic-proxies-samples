package eu.javaspecialists.books.dynamicproxies.ch03;

import java.util.Collection;
import java.util.function.IntFunction;
import java.util.stream.Stream;

// tag::ImmutableCollection[]
public interface ImmutableCollection<E> extends ImmutableIterable<E> {
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    Object[] toArray();
    <T> T[] toArray(T[] a);
    <T> T[] toArray(IntFunction<T[]> generator);
    boolean containsAll(Collection<?> c);
    Stream<E> stream();
    Stream<E> parallelStream();

    // mutators filtered away
    //    boolean add(E e);
    //    boolean remove(Object o);
    //    boolean addAll(java.util.Collection<? extends E> c);
    //    boolean removeAll(java.util.Collection<?> c);
    //    boolean removeIf(Predicate<? super E> filter);
    //    boolean retainAll(java.util.Collection<?> c);
    //    void clear();
}
// end::ImmutableCollection[]

