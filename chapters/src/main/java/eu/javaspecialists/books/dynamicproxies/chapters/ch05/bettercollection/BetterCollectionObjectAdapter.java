/*
 * Copyright (C) 2020 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz
 * licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */

package eu.javaspecialists.books.dynamicproxies.chapters.ch05.bettercollection;

import java.util.*;

// tag::listing[]
public class BetterCollectionObjectAdapter<E>
    implements BetterCollection<E> {
  private final Collection<E> adaptee;
  private final E[] seedArray;

  public BetterCollectionObjectAdapter(Collection<E> adaptee,
                                       E[] seedArray) {
    if (seedArray.length != 0)
      throw new IllegalArgumentException(
          "seedArray must be empty");

    this.adaptee = adaptee;
    this.seedArray = seedArray;
  }

  @Override
  public E[] toArray() {
    return adaptee.toArray(seedArray);
  }

  @Override
  public String toString() {
    return "--" + adaptee.toString() + "--";
  }

  // this is a typical problem with the object adapter design
  // pattern - we have to implement all the methods :-(
  @Override
  public int size() {
    return adaptee.size();
  }

  @Override
  public boolean isEmpty() {
    return adaptee.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return adaptee.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return adaptee.iterator();
  }

  @Override
  public <T> T[] toArray(T[] ts) {
    return adaptee.toArray(ts);
  }

  @Override
  public boolean add(E e) {
    return adaptee.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return adaptee.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return adaptee.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends E> es) {
    return adaptee.addAll(es);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return adaptee.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return adaptee.retainAll(c);
  }

  @Override
  public void clear() {
    adaptee.clear();
  }
}
// end::listing[]