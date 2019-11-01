/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.ch02.equals;

import eu.javaspecialists.books.dynamicproxies.ch02.virtual.*;
import org.junit.*;

import java.util.function.*;

import static org.junit.Assert.*;

public abstract class EqualsTest {
  private final Supplier<CustomMap<Integer, Integer>> realGenerator;
  private final UnaryOperator<CustomMap<Integer, Integer>> proxyGenerator;

  public EqualsTest(Supplier<CustomMap<Integer, Integer>> realGenerator,
                    UnaryOperator<CustomMap<Integer, Integer>> proxyGenerator) {
    this.realGenerator = realGenerator;
    this.proxyGenerator = proxyGenerator;
  }

  private CustomMap<Integer, Integer>[] create() {
    CustomMap<Integer, Integer> temp;
    return new CustomMap[] {
        temp = makeReal(),
        makeProxy(temp),
        temp = makeProxy(temp),
        temp = makeProxy(temp),
        makeProxy(temp),
        temp = makeReal(),
        makeProxy(temp),
        makeProxy(temp),
    };
  }
  private CustomMap<Integer, Integer> makeProxy(
      CustomMap<Integer, Integer> map) {
    return proxyGenerator.apply(map);
  }

  private CustomMap<Integer, Integer> makeReal() {
    CustomMap<Integer, Integer> map =
        realGenerator.get();
    map.put(2, 4);
    map.put(10, 100);
    map.put(16, 256);
    return map;
  }


  @Test
  public void reflexive() {
    for (CustomMap<Integer, Integer> x : create()) {
      assertTrue(x.equals(x));
    }
  }

  @Test
  public void symmetric() {
    CustomMap<Integer, Integer>[] objects = create();

    for (int i = 0; i < objects.length - 1; i++) {
      for (int j = i + 1; j < objects.length; j++) {
        CustomMap<Integer, Integer> x = objects[i];
        CustomMap<Integer, Integer> z = objects[j];
        testSymmetry(i + "," + j, x, z);
      }
    }
  }

  private void testSymmetry(String description,
                            CustomMap<?, ?> x,
                            CustomMap<?, ?> z) {
    assertSame(description, x.equals(z), z.equals(x));
  }

  @Test
  public void transitive() {
    CustomMap<?, ?>[] objects = create();

    for (int i = 0; i < objects.length; i++) {
      CustomMap<?, ?> x = objects[i];
      for (int j = 0; j < objects.length; j++) {
        CustomMap<?, ?> y = objects[j];
        for (int k = 0; k < objects.length; k++) {
          CustomMap<?, ?> z = objects[k];
          testTransitivity(i + "," + j + "," + k, x, y, z);
        }
      }
    }
  }

  private void testTransitivity(String description,
                                CustomMap<?, ?> x,
                                CustomMap<?, ?> y,
                                CustomMap<?, ?> z) {
    if (x.equals(y) && y.equals(z))
      assertTrue(description, x.equals(z));
  }

  @Test
  public void nullEquals() {
    for (CustomMap<?, ?> x : create()) {
      assertFalse(x.equals(null));
    }
  }


  @Test
  public void proxyEqualsProxy() {
    CustomMap<Integer, Integer> real = makeReal();
    CustomMap<Integer, Integer> proxy1 = makeProxy(real);
    CustomMap<Integer, Integer> proxy2 = makeProxy(real);
    assertTrue(proxy1.equals(proxy2));
    assertTrue(proxy2.equals(proxy1));
    assertTrue(proxy1.equals(proxy1));
    assertTrue(proxy2.equals(proxy2));
  }


  @Test
  public void proxyEqualsReal() {
    CustomMap<Integer, Integer> b = makeReal();
    CustomMap<Integer, Integer> c1 = makeProxy(b);
    assertTrue(c1.equals(b));
  }

  @Test
  public void realEqualsProxy() {
    CustomMap<Integer, Integer> real = makeReal();
    CustomMap<Integer, Integer> proxy = makeProxy(real);
    assertTrue(real.equals(proxy));
    assertTrue(real.equals(real));
  }

  @Test
  public void cascadingProxyEquals() {
    CustomMap<Integer, Integer> real = makeReal();
    CustomMap<Integer, Integer> proxy1 = makeProxy(real);
    CustomMap<Integer, Integer> proxy2 = makeProxy(proxy1);
    CustomMap<Integer, Integer> proxy3 = makeProxy(proxy2);
    assertTrue(real.equals(proxy1));
    assertTrue(real.equals(proxy2));
    assertTrue(real.equals(proxy3));
    assertTrue(proxy1.equals(real));
    assertTrue(proxy2.equals(real));
    assertTrue(proxy3.equals(real));
    assertTrue(proxy1.equals(proxy2));
    assertTrue(proxy1.equals(proxy3));
    assertTrue(proxy2.equals(proxy1));
    assertTrue(proxy2.equals(proxy3));
    assertTrue(proxy3.equals(proxy1));
    assertTrue(proxy3.equals(proxy2));
    assertTrue(proxy1.equals(proxy1));
    assertTrue(proxy2.equals(proxy2));
    assertTrue(proxy3.equals(proxy3));
  }

  @Test
  public void cascadingProxyEqualsWithD() {
    CustomMap<Integer, Integer> real = makeReal();
    CustomMap<Integer, Integer> proxy1 = makeProxy(real);
    CustomMap<Integer, Integer> proxy2 = makeProxy(real);
    CustomMap<Integer, Integer> proxy3 = makeProxy(proxy2);
    assertTrue(real.equals(proxy1));
    assertTrue(real.equals(proxy2));
    assertTrue(real.equals(proxy3));
    assertTrue(proxy1.equals(real));
    assertTrue(proxy2.equals(real));
    assertTrue(proxy3.equals(real));
    assertTrue(proxy1.equals(proxy2));
    assertTrue(proxy1.equals(proxy3));
    assertTrue(proxy2.equals(proxy1));
    assertTrue(proxy2.equals(proxy3));
    assertTrue(proxy3.equals(proxy1));
    assertTrue(proxy3.equals(proxy2));
    assertTrue(proxy1.equals(proxy1));
    assertTrue(proxy2.equals(proxy2));
    assertTrue(proxy3.equals(proxy3));
  }
}

