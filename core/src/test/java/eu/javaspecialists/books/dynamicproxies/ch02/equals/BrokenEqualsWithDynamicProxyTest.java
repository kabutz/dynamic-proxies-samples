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

package eu.javaspecialists.books.dynamicproxies.ch02.equals;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.ch02.virtual.*;
import org.junit.*;

public class BrokenEqualsWithDynamicProxyTest extends BrokenEqualsTest {
  public BrokenEqualsWithDynamicProxyTest() {
    super(map -> Proxies.simpleProxy(CustomMap.class, map));
  }

  // These tests do not work

  @Override
  @Test(expected = AssertionError.class)
  public void cascadingProxyEquals() {
    System.out.println("cascadingProxyEquals() broken");
    super.cascadingProxyEquals();
  }
  @Override
  @Test(expected = AssertionError.class)
  public void cascadingProxyEqualsWithD() {
    System.out.println("cascadingProxyEqualsWithD() broken");
    super.cascadingProxyEqualsWithD();
  }
  @Override
  @Test(expected = AssertionError.class)
  public void reflexive() {
    System.out.println("reflexive() broken");
    super.reflexive();
  }
  @Override
  @Test(expected = AssertionError.class)
  public void symmetric() {
    System.out.println("symmetric() broken");
    super.symmetric();
  }
  @Override
  @Test(expected = AssertionError.class)
  public void proxyEqualsProxy() {
    System.out.println("proxyEqualsProxy() broken");
    super.proxyEqualsProxy();
  }
  @Override
  @Test(expected = AssertionError.class)
  public void realEqualsProxy() {
    System.out.println("realEqualsProxy() broken");
    super.realEqualsProxy();
  }
}