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

package scratch;import java.lang.reflect.*;
import java.util.*;

public class MethodNamesTest {
  public static void main(String... args) throws Exception {
    print(Object.class);
    print(String.class);
    print(Integer.class);
    print(Long.class);
    for (Method method : ArrayList.class.getMethods()) {
      String name = method.getName();
//      if (name != name.intern()) throw new AssertionError(
//          "Mismatched name"
//      );
      System.out.println(name);
    }
    Thread.sleep(10000000);
  }
  private static void print(Class<?> clazz) throws NoSuchMethodException {
    System.out.println(System.identityHashCode(clazz.getMethod("hashCode").getName()));
  }
}
