/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
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

import org.junit.*;

import java.util.*;
import java.util.concurrent.*;

public class BetterCollectionObjectAdapterTest extends BetterCollectionTest {
  @Test
  public void testHashSet() {
    testHandCrafted(new HashSet<>());
  }
  @Test
  public void testConcurrentSkipListSet() {
    testHandCrafted(new ConcurrentSkipListSet<>());
  }
  @Test
  public void testConcurrentHashSet() {
    testHandCrafted(ConcurrentHashMap.newKeySet());
  }
  @Test
  public void testArrayList() {
    testHandCrafted(new ArrayList<>());
  }
  @Test
  public void testLinkedList() {
    testHandCrafted(new LinkedList<>());
  }
  @Test
  public void testVector() {
    testHandCrafted(new Vector<>());
  }
  @Test
  public void testConcurrentLinkedQueue() {
    testHandCrafted(new ConcurrentLinkedQueue<>());
  }
  @Test
  public void testArrayDeque() {
    testHandCrafted(new ArrayDeque<>());
  }
}
