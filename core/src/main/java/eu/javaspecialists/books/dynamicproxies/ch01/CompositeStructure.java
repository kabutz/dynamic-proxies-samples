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

import java.util.*;

public class CompositeStructure {
  // tag::listing[]
  public class Client {
    void execute(Component component) {
      component.operation();
    }
  }
  public abstract class Component {
    public abstract void operation();
    public boolean add(Component c) {
      return false;
    }
    public boolean remove(Component c) {
      return false;
    }
  }
  public class Leaf extends Component {
    @Override
    public void operation() {
      /* left to our imagination */
    }
  }
  public class Composite extends Component {
    private final Collection<Component> children =
        new ArrayList<>();
    public boolean add(Component c) {
      return children.add(c);
    }
    public boolean remove(Component c) {
      return children.remove(c);
    }
    @Override
    public void operation() {
      for (Component c : children) {
        c.operation();
      }
    }
  }
  // end::listing[]
}
