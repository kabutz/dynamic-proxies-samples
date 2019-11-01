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

package eu.javaspecialists.books.dynamicproxies.ch05.bettercollection;

import eu.javaspecialists.books.dynamicproxies.*;

import java.lang.reflect.*;
import java.util.*;

// tag::listing[]
public class BetterCollectionFactory {
  public static <E> BetterCollection<E> asBetterCollection(
      Collection<E> adaptee, Class<E> valueType) {
    return Proxies.adapt(BetterCollection.class, adaptee,
        // this anonymous inner class contains the method that
        // we want to adapt
        new AdaptationObject<>(adaptee, valueType));
  }
  public static class AdaptationObject<E> {
    private final Collection<E> adaptee;
    private final Class<E> valueType;
    public AdaptationObject(Collection<E> adaptee,
                            Class<E> valueType) {
      this.adaptee = adaptee;
      this.valueType = valueType;
    }
    public E[] toArray() {
      return adaptee.toArray((E[]) Array.newInstance(
          valueType, adaptee.size()));
    }
    // Whilst we are at it, we could also make it into
    // a checked collection, see java.util.Collections
    // for an example.
    public boolean add(E e) {
      if (!valueType.isInstance(e))
        throw new ClassCastException(
            "Attempt to insert " + e.getClass() +
                " value into collection " +
                "with value type " + valueType);
      return adaptee.add(e);
    }
    // addAll() left as an exercise for the reader :-)
  }
}
// end::listing[]