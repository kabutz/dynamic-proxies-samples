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
  public void testSimpleVTable() throws ReflectiveOperationException {
    VTable vt = VTables.newVTableExcludingObjectMethods(
        RealSubject.class, Subject.class
    );
    assertEquals(1, vt.size());

    Method subjectMethod = Subject.class.getMethod(
        "uppercaseTrim", String.class);
    Method realSubjectMethod = RealSubject.class.getMethod(
        "uppercaseTrim", String.class);
    Method lookup = vt.lookup(subjectMethod);
    assertEquals(lookup, realSubjectMethod);
    assertFalse(isOverloaded(vt, subjectMethod));
  }

  @Test
  public void testVTableIncludingObjectMethods() throws ReflectiveOperationException {
    VTable vt = VTables.newVTable(
        RealSubject.class, Subject.class
    );

    assertEquals(4, vt.size());

    Method subjectMethod = Subject.class.getMethod(
        "uppercaseTrim", String.class);
    Method realSubjectMethod = RealSubject.class.getMethod(
        "uppercaseTrim", String.class);
    Method lookup = vt.lookup(subjectMethod);
    assertEquals(lookup, realSubjectMethod);
    assertFalse(isOverloaded(vt, subjectMethod));
  }

  public static class ObjectAdapter {
    private boolean clearCalled = false;
    public void clear() {
      clearCalled = true;
    }
  }

  @Test
  public void testObjectAdapter() throws ReflectiveOperationException {
    VTable vt = VTables.newVTable(ObjectAdapter.class,
        Collection.class);

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
    VTable vt = VTables.newVTableExcludingObjectMethods(
        queue.getClass(), UnmodifiableQueue.class
    );

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
    VTable vt = VTables.newVTable(
        queue.getClass(), UnmodifiableQueue.class);

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
  public void testOverloading() throws ReflectiveOperationException {
    VTable vt = VTables.newVTable(ArrayList.class, List.class);

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
      throws ReflectiveOperationException {
    assertEquals(expected, isOverloaded(vt,
        List.class.getMethod(name, parameterTypes)
    ));
  }

  private final static Method findIndexMethod;
  private final static Field distinctNameField;

  static {
    try {
      findIndexMethod = VTable.class.getDeclaredMethod(
          "findIndex", Method.class);
      findIndexMethod.setAccessible(true);
      distinctNameField = VTable.class.getDeclaredField(
          "distinctName");
      distinctNameField.setAccessible(true);
    } catch (ReflectiveOperationException e) {
      throw new Error(e);
    }
  }

  /**
   * Returns true if method is overloaded; false otherwise.
   *
   * @throws IllegalArgumentException if method is not in VTable
   */
  private boolean isOverloaded(VTable vt, Method method) throws ReflectiveOperationException {
    int index = (int) findIndexMethod.invoke(vt, method);
    if (index < 0)
      throw new IllegalArgumentException("Method not found");
    return !((boolean[]) distinctNameField.get(vt))[index];
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
    VTable vt = VTables.newDefaultMethodVTable(Child.class);

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
  }

  public static class AllSupplier<E> {
    private final OptionalInt intValue;
    private final OptionalLong longValue;
    private final OptionalDouble doubleValue;
    private final Optional<E> value;

    public AllSupplier(OptionalInt intValue,
                       OptionalLong longValue,
                       OptionalDouble doubleValue,
                       Optional<E> value) {
      this.intValue = intValue;
      this.longValue = longValue;
      this.doubleValue = doubleValue;
      this.value = value;
    }

    public int getAsInt() {
      return intValue.getAsInt();
    }

    public long getAsLong() {
      return longValue.getAsLong();
    }

    public double getAsDouble() {
      return doubleValue.getAsDouble();
    }

    public E get() {
      return value.get();
    }

    public static class Builder<E> {
      private OptionalInt intValue = OptionalInt.empty();
      private OptionalLong longValue = OptionalLong.empty();
      private OptionalDouble doubleValue =
          OptionalDouble.empty();
      private Optional<E> value = Optional.empty();
      public Builder<E> setInt(int value) {
        intValue = OptionalInt.of(value);
        return this;
      }
      public Builder<E> setLong(long value) {
        longValue = OptionalLong.of(value);
        return this;
      }
      public Builder<E> setDouble(double value) {
        doubleValue = OptionalDouble.of(value);
        return this;
      }
      public Builder<E> set(E value) {
        this.value = Optional.of(value);
        return this;
      }
      public AllSupplier<E> build() {
        return new AllSupplier<>(intValue, longValue,
            doubleValue, value);
      }
    }
  }

  @Test
  public void multipleTargetInterfaces() throws NoSuchMethodException {
    VTable vt = VTables.newVTableExcludingObjectMethods(
        AllSupplier.class, IntSupplier.class, LongSupplier.class,
        DoubleSupplier.class, Supplier.class);

    assertEquals(4, vt.size());
    assertEquals(
        AllSupplier.class.getMethod("getAsInt"),
        vt.lookup(IntSupplier.class.getMethod("getAsInt"))
    );
    assertEquals(
        AllSupplier.class.getMethod("getAsLong"),
        vt.lookup(LongSupplier.class.getMethod("getAsLong"))
    );
    assertEquals(
        AllSupplier.class.getMethod("getAsDouble"),
        vt.lookup(DoubleSupplier.class.getMethod("getAsDouble"))
    );
    assertEquals(
        AllSupplier.class.getMethod("get"),
        vt.lookup(Supplier.class.getMethod("get"))
    );
  }

  private interface StringClone {
    String clone();
  }

  private interface CollectionClone {
    Collection<?> clone();
  }

  private interface ObjectClone {
    Object clone();
  }

  private static class SuperArrayDeque<E> extends ArrayDeque<E> {}

  private interface SuperArrayDequeClone {
    SuperArrayDeque<?> clone();
  }

  @Test
  public void mismatchedReturn() {
    System.out.println("StringClone");
    VTable vt1 = VTables.newVTableExcludingObjectMethods(
        ArrayDeque.class, StringClone.class);
    assertEquals(0, vt1.size());

    System.out.println("CollectionClone");
    VTable vt2 = VTables.newVTableExcludingObjectMethods(
        ArrayDeque.class, CollectionClone.class);
    assertEquals(1, vt2.size());

    System.out.println("ObjectClone");
    VTable vt3 = VTables.newVTableExcludingObjectMethods(
        ArrayDeque.class, ObjectClone.class);
    assertEquals(1, vt3.size());

    System.out.println("SuperArrayDequeClone");
    VTable vt4 = VTables.newVTableExcludingObjectMethods(
        ArrayDeque.class, SuperArrayDequeClone.class);
    assertEquals(0, vt4.size());
  }

  private static class TestIterable<E> implements Iterable<E> {
    private final Iterable<E> iterable;
    public TestIterable(Iterable<E> iterable) {
      this.iterable = iterable;
    }
    @Override
    public Iterator<E> iterator() {
      return iterable.iterator();
    }
  }

  @Test
  public void testDefaultMethods() {
    VTable vt = VTables.newDefaultMethodVTable(Iterable.class);
    assertEquals(2, vt.streamDefaultMethods().count());
  }
}
