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

package eu.javaspecialists.books.dynamicproxies.ch01;

import java.nio.*;
import java.util.*;
import java.util.concurrent.*;

public class DecoratorStructure {
  // tag::listing[]
  public class Client {
    void execute(Component component) {
      ConcreteDecoratorB decorator = new ConcreteDecoratorB(
          new ConcreteDecoratorA(component)
      );
      decorator.addedBehavior();
    }
  }
  public interface Component {
    void operation();
  }
  public class ConcreteComponent implements Component {
    @Override
    public void operation() {
      /* left to our imagination */
    }
  }
  public abstract class Decorator implements Component {
    private final Component component;
    public Decorator(Component component) {
      this.component = component;
    }
    @Override
    public void operation() {
      component.operation();
    }
  }
  public class ConcreteDecoratorA extends Decorator {
    // Added State
    private final byte[] buffer = new byte[8192];
    private final Map<String, ByteBuffer> cache =
        new ConcurrentHashMap<>();
    public ConcreteDecoratorA(Component component) {
      super(component);
    }
    @Override
    public void operation() {
      // use state to speed up or otherwise enhance Component
      super.operation();
    }
  }
  public class ConcreteDecoratorB extends Decorator {
    // Added State
    private final byte[] buffer = new byte[8192];
    private final Map<String, ByteBuffer> cache =
        new ConcurrentHashMap<>();
    public ConcreteDecoratorB(Component component) {
      super(component);
    }
    @Override
    public void operation() {
      super.operation();
    }
    public void addedBehavior() {
      // add behavior to operation()
      operation();
    }
  }
  // end::listing[]
}
