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

package eu.javaspecialists.books.dynamicproxies.chapters.ch06.appendableclass;

import java.io.*;

// tag::listing[]
public class TeeOutputStream extends OutputStream {
  private final OutputStream out1, out2;
  public TeeOutputStream(OutputStream out1, OutputStream out2) {
    this.out1 = out1;
    this.out2 = out2;
  }
  @Override
  public void write(int b) throws IOException {
    out1.write(b);
    out2.write(b);
  }
  @Override
  public void flush() throws IOException {
    out1.flush();
    out2.flush();
  }
  @Override
  public void close() throws IOException {
    out1.close();
    out2.close();
  }
}
// end::listing[]
