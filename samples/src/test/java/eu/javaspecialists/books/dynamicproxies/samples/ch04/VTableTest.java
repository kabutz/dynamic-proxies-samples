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

package eu.javaspecialists.books.dynamicproxies.samples.ch04;

import eu.javaspecialists.books.dynamicproxies.samples.ch03.*;
import eu.javaspecialists.books.dynamicproxies.util.*;
import org.junit.*;

import java.lang.reflect.*;

import static org.junit.Assert.*;

public class VTableTest {
  @Test
  public void testSimpleVTable() throws ReflectiveOperationException {
    VTable vt = VTables.newVTableExcludingObjectMethods(
        RealISODateParser.class, ISODateParser.class
    );
    assertEquals(1, vt.size());

    Method subjectMethod = ISODateParser.class.getMethod(
        "parse", String.class);
    Method realSubjectMethod = RealISODateParser.class.getMethod(
        "parse", String.class);
    Method lookup = vt.lookup(subjectMethod);
    assertEquals(lookup, realSubjectMethod);
  }

  @Test
  public void testVTableIncludingObjectMethods() throws ReflectiveOperationException {
    VTable vt = VTables.newVTable(
        RealISODateParser.class, ISODateParser.class
    );
    assertEquals(4, vt.size());

    Method subjectMethod = ISODateParser.class.getMethod(
        "parse", String.class);
    Method realSubjectMethod = RealISODateParser.class.getMethod(
        "parse", String.class);
    Method lookup = vt.lookup(subjectMethod);
    assertEquals(lookup, realSubjectMethod);
  }
}