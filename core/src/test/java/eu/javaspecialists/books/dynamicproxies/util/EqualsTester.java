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

import static org.junit.Assert.*;

public class EqualsTester {
  public static void test(Object[] objects) {
    reflexive(objects);
    quadratic(objects);
    transitive(objects);
    nullFalse(objects);
  }

  // reflexive - linear    }
  private static void reflexive(Object[] objects) {
    for (Object x : objects) {
      assertTrue("x.equals(x) is false", x.equals(x));
    }
  }

  // symmetric - quadratic
  private static void quadratic(Object[] objects) {
    for (Object x : objects) {
      for (Object y : objects) {
        assertEquals(x.equals(y), y.equals(x));
      }
    }
  }

  // transitive - cubic
  private static void transitive(Object[] objects) {
    for (Object x : objects) {
      for (Object y : objects) {
        for (Object z : objects) {
          if (x.equals(y) && y.equals(z)) {
            assertTrue(x.equals(z));
          }
        }
      }
    }
  }

  // null comparison is false - linear    }
  private static void nullFalse(Object[] objects) {
    for (Object x : objects) {
      assertFalse(x.equals(null));
    }
  }
}