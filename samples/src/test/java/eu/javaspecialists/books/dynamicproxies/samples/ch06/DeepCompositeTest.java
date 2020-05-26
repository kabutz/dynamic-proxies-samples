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

package eu.javaspecialists.books.dynamicproxies.samples.ch06;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.handlers.*;
import org.junit.*;

import static org.junit.Assert.*;

// To run this test, add the following to the VM arguments:
// --add-opens eu.javaspecialists.books.dynamicproxies.samples/eu.javaspecialists.books.dynamicproxies.samples.ch06=eu.javaspecialists.books.dynamicproxies
// --add-reads eu.javaspecialists.books.dynamicproxies=eu.javaspecialists.books.dynamicproxies.samples
public class DeepCompositeTest {
  public interface TestComponent extends BaseComponent<TestComponent> {
    void accept(Visitor visitor);
  }

  public interface CompositeA extends TestComponent {
    @Override
    default void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }
  public interface CompositeB extends TestComponent {
    @Override
    default void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }
  public class Leaf implements TestComponent {
    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }
  public interface Visitor {
    void visit(CompositeA c);
    void visit(CompositeB c);
    void visit(Leaf l);
  }

  private static class CountingVisitor implements Visitor {
    private int counta, countb, countleaves;
    @Override
    public void visit(CompositeA c) {
      counta++;
    }
    @Override
    public void visit(CompositeB c) {
      countb++;
    }
    @Override
    public void visit(Leaf l) {
      countleaves++;
    }
  }

  @Test
  public void testDeepInterfaceHierarchy() {
    CompositeA comp = create(CompositeA.class);
    check(1, 0, 0, comp);

    CompositeB c2 = create(CompositeB.class);
    comp.add(c2);
    check(1, 1, 0, comp);

    c2.add(new Leaf());
    c2.add(new Leaf());
    c2.add(new Leaf());
    check(1, 1, 3, comp);

    CompositeB c3 = create(CompositeB.class);
    c2.add(c3);
    check(1, 2, 3, comp);

    c3.add(new Leaf());
    c3.add(new Leaf());
    c3.add(new Leaf());
    check(1, 2, 6, comp);
  }

  private <T extends TestComponent> T create(Class<T> clazz) {
    return Proxies.compose(clazz, TestComponent.class);
  }

  private void check(int a, int b, int leaves, TestComponent comp) {
    CountingVisitor cv1 = new CountingVisitor();
    comp.accept(cv1);
    assertEquals(a, cv1.counta);
    assertEquals(b, cv1.countb);
    assertEquals(leaves, cv1.countleaves);
  }
}
