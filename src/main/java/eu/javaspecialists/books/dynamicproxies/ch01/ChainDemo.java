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

package eu.javaspecialists.books.dynamicproxies.ch01;

import java.util.*;

public class ChainDemo {
  private Map<String, String> map1 = new HashMap<>();
  private Map<String, String> map2 = new HashMap<>();
  private Map<String, String> map3 = new HashMap<>();

  public static void main(String... args) {
    ChainDemo demo = new ChainDemo();
    demo.noChain("test");
  }

  private void noChain(String key) {
    // tag::noChain()[]
    var item = map1.get(key);
    if (item != null) process(item);
    else {
      item = map2.get(key);
      if (item != null) process(item);
      else {
        item = map3.get(key); // ad nauseum
      }
    }
    // end::noChain()[]
  }


  // tag::Handler[]
  public abstract class Handler {
    private final Handler next;
    public Handler(Handler next) {
      this.next = next;
    }
    public void handle(String key) {
      if (next != null) next.handle(key);
    }
  }
  // end::Handler[]

  // tag::MapHandler[]
  public class MapHandler extends Handler {
    private final Map<String, String> map;
    public MapHandler(Map<String, String> map, Handler next) {
      super(next);
      this.map = map;
    }
    @Override
    public void handle(String key) {
      String item = map.get(key);
      if (item != null) process(item);
      else super.handle(key);
    }
  }
  // end::MapHandler[]

  // tag::chain[]
  private final Handler chain =
      new MapHandler(map1,
          new MapHandler(map2,
              new MapHandler(map3, null)));
  // end::chain[]

  private void chain(String key) {
    // tag::chain()[]
    chain.handle(key);
    // end::chain()[]
  }

  private static void process(String item) {
    System.out.println("Processing: " + item);
  }
}
