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

package eu.javaspecialists.books.dynamicproxies.chapters.ch03.enhancedstream;

import eu.javaspecialists.books.dynamicproxies.util.*;
import org.junit.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import static org.junit.Assert.*;

public class EnhancedStreamTest {
  @Test
  public void testMethods() {
    Stream.of(ArrayDeque.class, ArrayList.class,
        LinkedList.class, ConcurrentSkipListSet.class,
        ConcurrentLinkedDeque.class)
        .forEach(this::checkMethods);
  }
  private void checkMethods(Class<?> clazz) {
    EnhancedStream.of(clazz.getMethods())
        .distinct(EnhancedStreamDemo.HASH_CODE,
            EnhancedStreamDemo.EQUALS, EnhancedStreamDemo.MERGE)
        .forEach(method -> checkMethod(clazz, method));
    EnhancedStream.of(clazz.getMethods())
        .distinct(MethodKey::new, EnhancedStreamDemo.MERGE)
        .forEach(method -> checkMethod(clazz, method));
  }
  private void checkMethod(Class<?> clazz, Method method) {
    try {
      assertEquals(method, clazz.getMethod(method.getName(),
          method.getParameterTypes()));
    } catch (NoSuchMethodException e) {
      throw new AssertionError(e);
    }
  }

  private static final ToIntFunction<String> HASH_CODE =
      s -> s.toUpperCase().hashCode();
  private static final BiPredicate<String, String> EQUALS =
      (s1, s2) -> s1.toUpperCase().equals(s2.toUpperCase());


  @Test
  public void testBeaches() {
    String beaches1 =
        EnhancedStream.of("Kalathas", "Stavros",
            "STAVROS", "marathi", "kalathas", "baLos", "Balos")
            .distinct(HASH_CODE, EQUALS, BeachDistinctify.MERGE)
            .collect(Collectors.joining(", "));

    assertEquals("kalathas, Stavros, marathi, baLos", beaches1);

    String beaches2 =
        EnhancedStream.of("Kalathas", "Stavros",
            "STAVROS", "marathi", "kalathas", "baLos", "Balos")
            .distinct(String::toUpperCase,
                BeachDistinctify.MERGE)
            .collect(Collectors.joining(", "));

    assertEquals("kalathas, Stavros, marathi, baLos", beaches2);
  }
}