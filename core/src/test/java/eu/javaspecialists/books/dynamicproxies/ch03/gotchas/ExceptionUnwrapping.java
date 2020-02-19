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

package eu.javaspecialists.books.dynamicproxies.ch03.gotchas;

import eu.javaspecialists.books.dynamicproxies.*;
import org.junit.*;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class ExceptionUnwrapping {
  @Test
  public void testRuntimeExceptions() {
    try {
      Collection<String> test = Proxies.simpleProxy(
          Collection.class, List.of());
      test.clear();
      fail("Expected an UnsupportedOperationException");
    } catch(UnsupportedOperationException success) {
    }
  }
  @Test
  public void testIOException() {
    try {
      FileReaderWizard wiz = Proxies.simpleProxy(
          FileReaderWizard.class, new FileReaderWizard() {
            @Override
            public void open() throws IOException {
              throw new IOException("File has magically vanished");
            }
          }
      );
      wiz.open();
      fail("Expected an IOException");
    } catch(IOException success) {
    }
  }

  public interface FileReaderWizard {
    void open() throws IOException;
  }
}
