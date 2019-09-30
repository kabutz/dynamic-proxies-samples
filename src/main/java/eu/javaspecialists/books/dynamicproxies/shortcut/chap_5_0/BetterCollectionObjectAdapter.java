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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap_5_0;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Sven Ruppert on 05.01.14.
 */
public class BetterCollectionObjectAdapter<T> implements BetterCollection<T> {
    private final Collection<T> adaptee;
    private final Class<T> valueType;

    public BetterCollectionObjectAdapter(Collection<T> adaptee,
                                         Class<T> valueType) {
        this.adaptee = adaptee;
        this.valueType = valueType;
    }

    public T[] toArray() {
        return adaptee.toArray(
            (T[]) Array.newInstance(valueType, adaptee.size()));
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

    public Iterator<T> iterator() {
        return adaptee.iterator();
    }

    public <T> T[] toArray(T[] ts) {
        return adaptee.toArray(ts);
    }

    public boolean add(T t) {
        return adaptee.add(t);
    }

    public boolean remove(Object o) {
        return adaptee.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return adaptee.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> ts) {
        return adaptee.addAll(ts);
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


    public static void main(String[] args) {
        BetterCollection<String> names =
            new BetterCollectionObjectAdapter<>(
                new LinkedList<>(), String.class);
        names.add("Wolfgang");
        names.add("Leander");
        names.add("Klaus");
        names.add("Reinhard");
        String[] nameArray = names.toArray();
        for (String s : nameArray) {
            System.out.println(s);
        }
    }

}
