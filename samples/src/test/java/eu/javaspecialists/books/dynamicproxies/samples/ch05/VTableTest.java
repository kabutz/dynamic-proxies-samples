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

package eu.javaspecialists.books.dynamicproxies.samples.ch05;

import eu.javaspecialists.books.dynamicproxies.samples.ch05.bettercollection.*;
import eu.javaspecialists.books.dynamicproxies.util.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class VTableTest {
  @Test
  public void objectAdapterTest() {
    HashSet<String> adaptee = new HashSet<>();
    String[] seedArray = new String[0];
    Object adapter =
        new BetterCollectionFactory.AdaptationObject<>(
            adaptee, seedArray);
    Class<?> target = BetterCollection.class;

    VTable adapterVT =
        VTables.newVTable(adapter.getClass(), target);
    System.out.println("adapterVT:");
    adapterVT.stream().forEach(System.out::println);
    assertEquals(5, adapterVT.size());

    VTable adapteeVT =
        VTables.newVTableExcludingObjectMethods(
            adaptee.getClass(), target
        );
    System.out.println("adapteeVT:");
    adapteeVT.stream().forEach(System.out::println);
    assertEquals(21, adapteeVT.size());

    VTable defaultVT = VTables.newDefaultMethodVTable(target);
    System.out.println("defaultVT:");
    defaultVT.streamDefaultMethods().forEach(System.out::println);
    assertEquals(7, defaultVT.streamDefaultMethods().count());
  }
}