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

package eu.javaspecialists.books.dynamicproxies.chapters.ch01;

import java.util.*;
import java.util.function.*;

public class ChainDemo {
  private Map<String, String> map1 = new HashMap<>();
  private Map<String, String> map2 = new HashMap<>();
  private Map<String, String> map3 = new HashMap<>();

  public static void main(String... args) {
    ChainDemo demo = new ChainDemo();
    demo.handleWithoutChain("test", ChainDemo::process);
    demo.handleWithChain("test", ChainDemo::process);
  }

  // tag::handleWithoutChain()[]
  public void handleWithoutChain(
      String key, Consumer<String> processor) {
    var item = map1.get(key);
    if (item != null) processor.accept(item);
    else {
      item = map2.get(key);
      if (item != null) processor.accept(item);
      else {
        item = map3.get(key); // ad nauseum
      }
    }
  }
  // end::handleWithoutChain()[]

  // tag::chain[]
  private final Handler chain =
      new MapHandler(map1,
          new MapHandler(map2,
              new MapHandler(map3, null)));
  // end::chain[]

  // tag::handleWithChain()[]
  private void handleWithChain(
      String key, Consumer<String> processor) {
    chain.handle(key, processor);
  }
  // end::handleWithChain()[]

  private static void process(String item) {
    System.out.println("Processing: " + item);
  }
}
