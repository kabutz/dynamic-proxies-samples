/*
 * Copyright (c) 2014. Heinz Max Kabutz , Sven Ruppert
 *   We licenses
 *   this file to you under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License. You may
 *   obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.shortcut.chap_6_0;

import eu.javaspecialists.books.dynamicproxies.shortcut.chap_5_0.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by Sven Ruppert on 05.01.14.
 */
public class BetterCollectionFactory {

  public static <T> BetterCollection<T> asBetterCollection(
    final Collection<T> adaptee, final Class<T> valueType) {


    return DynamicObjectAdapterFactory.adapt(adaptee,
      BetterCollection.class,
// this anonymous inner class contains the method that
// we want to adapt
      new Object() {
        public T[] toArray() {
          return adaptee.toArray((T[]) Array.newInstance(
            valueType, adaptee.size()));
        }

        // Whilst we are at it, we could also make it into a
// checked collection, see java.util.Collections for
// an example.
        public boolean add(T o) {
          if (!valueType.isInstance(o))
            throw new ClassCastException("Attempt to insert " +
              o.getClass() +
              " value into collection with value type " + valueType);
          return adaptee.add(o);
        }
// addAll left as an exercise for the reader :-)
      });
  }


  public static void main(String[] args) {
    BetterCollection<String> names = asBetterCollection(
      new ArrayList<>(), String.class);
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
