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

package eu.javaspecialists.books.dynamicproxies.ch05;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.ch05.singer.*;
import org.junit.*;
import org.junit.rules.*;

public class AdapterTest {
  @Rule
  public final ExpectedException thrown =
      ExpectedException.none();
  @Test
  public void testBadAdapter() {
    Rapper rapper = new Rapper();
    thrown.expect(IllegalArgumentException.class);
    Singer singer = Proxies.adapt(Singer.class, rapper,
        new Object() {
          public void sing() {
            System.out.println("Making him sing");
            rapper.talk();
          }
        });
    System.out.println("Won't see this");
    singer.sing();
  }
  @Test
  public void testGoodAdapter() {
    Rapper rapper = new Rapper();
    Singer singer = Proxies.adapt(Singer.class, rapper,
        new SingingRapperObjectAdapter(rapper) {
          public void sing() {
            System.out.println("Making him sing");
            super.sing();
          }
        });
    singer.sing();
  }
}
