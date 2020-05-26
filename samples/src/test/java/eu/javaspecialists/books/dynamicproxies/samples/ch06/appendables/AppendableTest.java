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

package eu.javaspecialists.books.dynamicproxies.samples.ch06.appendables;

import java.io.*;

import static org.junit.Assert.*;

public class AppendableTest {

  protected <E extends Appendable & Closeable & Flushable> void test(
      E tee, StringWriter sw1, StringWriter sw2,
      StringWriter sw3) throws IOException {
    var out = new PrintWriter(new WriterAdapter<>(tee));
    out.println("Hello World");
    out.flush();

    tee.append("TestingAppender")
        .append(System.lineSeparator())
        .append("Does this work?")
        .append(System.lineSeparator());
    tee.flush();
    tee.close();

    assertEquals(sw1.toString(), sw2.toString());
    assertEquals(sw1.toString(), sw3.toString());
    assertEquals(
        String.format(
            "Hello World%nTestingAppender%nDoes this work?%n"),
        sw1.toString());
  }
}