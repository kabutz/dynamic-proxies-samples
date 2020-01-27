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

package eu.javaspecialists.books.dynamicproxies.ch05.bettercollection;

import java.util.*;

// tag::listing[]
public class BetterArrayList<E> extends ArrayList<E> {
  private final E[] seedArray;

  public BetterArrayList(E[] seedArray) {
    if (seedArray.length != 0)
      throw new IllegalArgumentException(
          "seedArray must be empty");
    this.seedArray = seedArray;
  }

  @Override
  public E[] toArray() {
    // NOTE: Shipilev showed that this is the fastest way to
    // create a typed array from a collection:
    // https://shipilev.net/blog/2016/arrays-wisdom-ancients/
    return toArray(seedArray);
  }

  @Override
  public String toString() {
    return "--" + super.toString() + "--";
  }
}
// end::listing[]
