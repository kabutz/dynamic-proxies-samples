/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.ch05;

// tag::BetterCollectionObjectAdapter[]
import java.lang.reflect.*;
import java.util.*;

public class BetterCollectionObjectAdapter<E> implements BetterCollection<E> {
    private final Collection<E> adaptee;
    private final Class<E> valueType;

    public BetterCollectionObjectAdapter(Collection<E> adaptee,
                                         Class<E> valueType) {
        this.adaptee = adaptee;
        this.valueType = valueType;
    }

    public E[] toArray() {
        return adaptee.toArray(
                (E[]) Array.newInstance(valueType, adaptee.size()));
    }

    // this is a typical problem with the Object Adapter Design
    // Pattern - you have implement all the methods :-(
    public int size() {
        return adaptee.size();
    }

    public boolean isEmpty() {
        return adaptee.isEmpty();
    }

    public boolean contains(Object o) {
        return adaptee.contains(o);
    }

    public Iterator<E> iterator() {
        return adaptee.iterator();
    }

    public <T> T[] toArray(T[] ts) {
        return adaptee.toArray(ts);
    }

    public boolean add(E e) {
        return adaptee.add(e);
    }

    public boolean remove(Object o) {
        return adaptee.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return adaptee.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> es) {
        return adaptee.addAll(es);
    }

    public boolean removeAll(Collection<?> c) {
        return adaptee.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return adaptee.retainAll(c);
    }

    public void clear() {
        adaptee.clear();
    }
}
// end::BetterCollectionObjectAdapter[]