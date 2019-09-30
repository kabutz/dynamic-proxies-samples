/*
 * Copyright (c) 2014. Heinz Max Kabutz , Sven Ruppert
 *   We licenses
 *   this file to you under the Apache License, Version 2.0 (the
 * "License");
 *   you may not use this file except in compliance with the License.
 * You may
 *   obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.shortcut.chap09_1.adapterdemo;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by Sven Ruppert on 19.12.2014.
 */
public class SynchronizedList<T> implements List<T> {

    private List<T> liste = new ArrayList<>();
    private Object LOCK;

    public int size() {
        synchronized (LOCK) {
            return liste.size();
        }
    }

    public boolean add(T t) {
        synchronized (LOCK) {
            return liste.add(t);
        }
    }

    public ListIterator<T> listIterator(int index) {
        synchronized (LOCK) {
            return liste.listIterator(index);
        }
    }

    public Spliterator<T> spliterator() {
        synchronized (LOCK) {
            return liste.spliterator();
        }
    }

    //########### and so on......

    public Iterator<T> iterator() {
        return liste.iterator();
    }

    public Stream<T> parallelStream() {
        return liste.parallelStream();
    }

    public int indexOf(Object o) {
        return liste.indexOf(o);
    }

    public boolean remove(Object o) {
        return liste.remove(o);
    }

    public void forEach(Consumer<? super T> action) {
        liste.forEach(action);
    }

    public <T1> T1[] toArray(T1[] a) {
        return liste.toArray(a);
    }

    public void sort(Comparator<? super T> c) {
        liste.sort(c);
    }

    public ListIterator<T> listIterator() {
        return liste.listIterator();
    }

    public boolean isEmpty() {
        return liste.isEmpty();
    }

    public Stream<T> stream() {
        return liste.stream();
    }

    public boolean retainAll(Collection<?> c) {
        return liste.retainAll(c);
    }

    public void add(int index, T element) {
        liste.add(index, element);
    }

    public boolean contains(Object o) {
        return liste.contains(o);
    }

    public T get(int index) {
        return liste.get(index);
    }

    public boolean containsAll(Collection<?> c) {
        return liste.containsAll(c);
    }

    public void replaceAll(UnaryOperator<T> operator) {
        liste.replaceAll(operator);
    }

    public boolean removeAll(Collection<?> c) {
        return liste.removeAll(c);
    }

    public int lastIndexOf(Object o) {
        return liste.lastIndexOf(o);
    }

    public boolean removeIf(Predicate<? super T> filter) {
        return liste.removeIf(filter);
    }

    public void clear() {
        liste.clear();
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return liste.addAll(index, c);
    }

    public T set(int index, T element) {
        return liste.set(index, element);
    }

    public Object[] toArray() {
        return liste.toArray();
    }

    public T remove(int index) {
        return liste.remove(index);
    }

    public boolean addAll(Collection<? extends T> c) {
        return liste.addAll(c);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return liste.subList(fromIndex, toIndex);
    }
}
