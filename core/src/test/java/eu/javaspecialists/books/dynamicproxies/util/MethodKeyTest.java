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

import org.junit.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import static org.junit.Assert.*;

public class MethodKeyTest {
  @Test
  public void hashCodeIsConsistent() {
    for (Method method : LinkedList.class.getMethods()) {
      assertEquals(method.hashCode(), method.hashCode());
    }
  }
  @Test
  public void hashCodeOverloadedMethodsDifferent() {
    long distinctHashes =
        Stream.of(LinkedList.class.getMethods())
            .filter(method -> method.getName().equals("wait"))
            .map(MethodKey::new)
            .mapToInt(Object::hashCode)
            .distinct()
            .count();
    assertEquals(3, distinctHashes);
  }

  @Test
  public void equalsFollowsContract() {
    MethodKey[] methodKeys =
        Stream.of(LinkedList.class.getMethods())
            .map(MethodKey::new)
            .toArray(MethodKey[]::new);

    EqualsTester.test(methodKeys);
  }

  @Test
  public void compareFollowsContract() {
    MethodKey[] methodKeys =
        Stream.of(LinkedList.class.getMethods())
            .map(MethodKey::new)
            .toArray(MethodKey[]::new);

    CompareTester.test(methodKeys);
  }

  @Test
  public void nullPointers() {
    try {
      new MethodKey(Object.class, "toString", (Class<?>[])null);
      fail("Expected NullPointerException");
    } catch(NullPointerException expected) {}
    try {
      new MethodKey(null, "toString");
      fail("Expected NullPointerException");
    } catch(NullPointerException expected) {}
  }

  @Test
  public void matches() throws ReflectiveOperationException {
    checkMatches(Object.class, "hashCode");
    checkMatches(Object.class, "equals", Object.class);
    checkMatches(List.class, "set", int.class, Object.class);

    Method ourHashCodeMethod = getClass().getMethod("hashCode");
    MethodKey ourMethodKey = new MethodKey(ourHashCodeMethod);
    assertEquals(ourMethodKey, new MethodKey(Object.class, "hashCode"));

    MethodKey objectMethodKey = new MethodKey(Object.class.getMethod("hashCode"));
    assertEquals(objectMethodKey, new MethodKey(Object.class, "hashCode"));

    assertEquals(ourMethodKey, objectMethodKey);

    assertTrue(objectMethodKey.matches(Object.class.getMethod("hashCode")));
    assertTrue(ourMethodKey.matches(Object.class.getMethod("hashCode")));
    assertTrue(objectMethodKey.matches(getClass().getMethod("hashCode")));
    assertTrue(ourMethodKey.matches(getClass().getMethod("hashCode")));
  }

  @Override
  public int hashCode() {
    return 0;
  }

  private void checkMatches(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws ReflectiveOperationException {
    Method method = clazz.getMethod(methodName, parameterTypes);
    MethodKey methodKey = new MethodKey(method);
    assertEquals(methodKey, new MethodKey(clazz, methodName, parameterTypes));
    assertTrue(methodKey.matches(method));
    assertTrue(methodKey.matches(clazz.getMethod(methodName, parameterTypes)));
  }
}
