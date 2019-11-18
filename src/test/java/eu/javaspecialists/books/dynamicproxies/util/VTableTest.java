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

package eu.javaspecialists.books.dynamicproxies.util;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.ch03.*;
import eu.javaspecialists.books.dynamicproxies.ch05.bettercollection.*;
import org.junit.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import static org.junit.Assert.*;

public class VTableTest {
  @Test
  public void testSimpleVTable() throws NoSuchMethodException {
    VTable vt = new VTable.Builder(RealSubject.class)
                    .excludeObjectMethods()
                    .addTargetInterface(Subject.class)
                    .build();

    assertEquals(1, vt.size());

    Method subjectMethod = Subject.class.getMethod(
        "uppercaseTrim", String.class);
    Method realSubjectMethod = RealSubject.class.getMethod(
        "uppercaseTrim", String.class);
    Method lookup = vt.lookup(subjectMethod);
    assertEquals(lookup, realSubjectMethod);
    assertFalse(vt.isOverloaded(subjectMethod));
  }

  public static class ObjectAdapter {
    private boolean clearCalled = false;
    public void clear() {
      clearCalled = true;
    }
  }

  @Test
  public void testObjectAdapter() throws ReflectiveOperationException {
    VTable vt = new VTable.Builder(ObjectAdapter.class)
                    .addTargetInterface(Collection.class)
                    .build();

    Method clear = vt.lookup(
        Collection.class.getMethod("clear")
    );
    ObjectAdapter adapter = new ObjectAdapter();
    clear.invoke(adapter);
    assertTrue(adapter.clearCalled);
  }

  public interface UnmodifiableQueue<E> {
    E peek();
    E element();
    int size();
    String toString();
  }

  @Test
  public void testFilter() throws ReflectiveOperationException {
    Queue<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < 5; i++) {
      queue.add(i);
    }
    VTable vt = new VTable.Builder(queue.getClass())
                    .addTargetInterface(UnmodifiableQueue.class)
                    .excludeObjectMethods()
                    .build();

    assertEquals(0,
        vt.lookup(UnmodifiableQueue.class.getMethod("peek")).invoke(queue));
    assertEquals(0,
        vt.lookup(UnmodifiableQueue.class.getMethod("element")).invoke(queue));
    assertEquals(5,
        vt.lookup(UnmodifiableQueue.class.getMethod("size")).invoke(queue));
    assertEquals("[0, 1, 2, 3, 4]",
        vt.lookup(UnmodifiableQueue.class.getMethod("toString")).invoke(queue));

    assertNull(vt.lookup(Object.class.getMethod("hashCode")));
    assertNull(vt.lookup(Object.class.getMethod("equals",
        Object.class)));
  }
  @Test
  public void testFilterWithObjectMethods() throws ReflectiveOperationException {
    Queue<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < 5; i++) {
      queue.add(i);
    }
    VTable vt = new VTable.Builder(queue.getClass())
                    .addTargetInterface(UnmodifiableQueue.class)
                    .build();

    Class<?> clazz = UnmodifiableQueue.class;
    assertEquals(0, invoke(vt, clazz, "peek", queue));
    assertEquals(0, invoke(vt, clazz, "element", queue));
    assertEquals(5, invoke(vt, clazz, "size", queue));
    assertEquals("[0, 1, 2, 3, 4]",
        invoke(vt, clazz, "toString", queue));

    assertNotNull(vt.lookup(Object.class.getMethod("hashCode")));
    assertNotNull(vt.lookup(Object.class.getMethod("equals",
        Object.class)));
  }

  private static Object invoke(
      VTable vt, Class<?> clazz, String method, Object component)
      throws ReflectiveOperationException {
    return vt.lookup(clazz.getMethod(method)).invoke(component);
  }

  @Test
  public void testOverloading() throws NoSuchMethodException {
    VTable vt = new VTable.Builder(ArrayList.class)
                    .addTargetInterface(List.class)
                    .build();

    overloaded(true, vt, "add", Object.class);
    overloaded(true, vt, "add", int.class, Object.class);
    overloaded(true, vt, "addAll", int.class, Collection.class);
    overloaded(true, vt, "addAll", Collection.class);
    overloaded(false, vt, "clear");
    overloaded(false, vt, "contains", Object.class);
    overloaded(false, vt, "containsAll", Collection.class);
    overloaded(false, vt, "copyOf", Collection.class);
    overloaded(false, vt, "equals", Object.class);
    overloaded(false, vt, "forEach", Consumer.class);
    overloaded(false, vt, "get", int.class);
    overloaded(false, vt, "hashCode");
    overloaded(false, vt, "indexOf", Object.class);
    overloaded(false, vt, "isEmpty");
    overloaded(false, vt, "iterator");
    overloaded(false, vt, "lastIndexOf", Object.class);
    overloaded(true, vt, "listIterator");
    overloaded(true, vt, "listIterator", int.class);
    overloaded(true, vt, "of");
    overloaded(true, vt, "of", Object.class);
    overloaded(true, vt, "of", Object.class, Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class, Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class, Object.class, Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class, Object.class, Object.class, Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class, Object.class, Object.class, Object.class
        , Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class, Object.class, Object.class, Object.class
        , Object.class, Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class, Object.class, Object.class, Object.class
        , Object.class, Object.class, Object.class);
    overloaded(true, vt, "of", Object.class, Object.class,
        Object.class, Object.class, Object.class, Object.class
        , Object.class, Object.class, Object.class,
        Object.class);
    overloaded(true, vt, "of", Object[].class);
    overloaded(false, vt, "parallelStream");
    overloaded(true, vt, "remove", Object.class);
    overloaded(true, vt, "remove", int.class);
    overloaded(false, vt, "removeAll", Collection.class);
    overloaded(false, vt, "removeIf", Predicate.class);
    overloaded(false, vt, "replaceAll", UnaryOperator.class);
    overloaded(false, vt, "retainAll", Collection.class);
    overloaded(false, vt, "set", int.class, Object.class);
    overloaded(false, vt, "size");
    overloaded(false, vt, "sort", Comparator.class);
    overloaded(false, vt, "spliterator");
    overloaded(false, vt, "stream");
    overloaded(false, vt, "subList", int.class, int.class);
    overloaded(true, vt, "toArray");
    overloaded(true, vt, "toArray", Object[].class);
    overloaded(true, vt, "toArray", IntFunction.class);
  }
  private void overloaded(boolean expected, VTable vt,
                          String name,
                          Class<?>... parameterTypes)
      throws NoSuchMethodException {
    assertEquals(expected, vt.isOverloaded(
        List.class.getMethod(name, parameterTypes)
    ));
  }

  public interface Parent {
    default CharSequence get() {
      return "Parent";
    }
  }

  public interface Child extends Parent {
    @Override
    default String get() {
      return "Child";
    }
  }

  @Test
  public void testCovariantReturnTypesDefaultMethods()
      throws Throwable {
    VTable vt = new VTable.Builder(Child.class)
                    .excludeObjectMethods()
                    .addTargetInterface(Child.class)
                    .includeDefaultMethods()
                    .build();

    assertEquals(1, vt.size());

    Child child = Proxies.simpleProxy(Child.class, null);

    Method parentGetMethod = Parent.class.getMethod("get");
    assertEquals(CharSequence.class,
        parentGetMethod.getReturnType());
    assertEquals(String.class,
        vt.lookup(parentGetMethod).getReturnType());
    assertEquals("Child", vt.lookupDefaultMethod(parentGetMethod)
                              .bindTo(child)
                              .invokeWithArguments((Object[]) null));

    Method childGetMethod = Child.class.getMethod("get");
    assertEquals(String.class, childGetMethod.getReturnType());
    assertEquals(String.class,
        vt.lookup(childGetMethod).getReturnType());
    assertEquals("Child", vt.lookupDefaultMethod(childGetMethod)
                              .bindTo(child)
                              .invokeWithArguments((Object[]) null));
  }

  @Test
  public void objectAdapterTest() {
    HashSet<String> adaptee = new HashSet<>();
    String[] seedArray = new String[0];
    Object adapter =
        new BetterCollectionFactory.AdaptationObject<>(adaptee
            , seedArray);
    Class<?> target = BetterCollection.class;

    VTable adapterMap =
        new VTable.Builder(adapter.getClass()) //
            // AdaptationObject.class
                            .addTargetInterface(target) //
            // BetterCollection.class
                            .build();
    System.out.println("adapterMap:");
    adapterMap.stream().forEach(System.out::println);
    assertEquals(5, adapterMap.size());

    VTable adapteeMap = new VTable.Builder(adaptee.getClass())
                            .addTargetInterface(target)
                            .build();
    System.out.println("adapteeMap:");
    adapteeMap.stream().forEach(System.out::println);
    assertEquals(22, adapteeMap.size());

    VTable defaultMap = new VTable.Builder(target)
                            .addTargetInterface(target)
                            .excludeObjectMethods()
                            .includeDefaultMethods()
                            .build();
    System.out.println("defaultMap:");
    defaultMap.streamDefaultMethods().forEach(System.out::println);
  }
}
