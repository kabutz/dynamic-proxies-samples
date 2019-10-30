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

package eu.javaspecialists.books.dynamicproxies.ch06.appendables;

import eu.javaspecialists.books.dynamicproxies.ch06.*;

import java.io.*;
import java.util.*;

// tag::listing[]
public class TeeOutputStream extends OutputStream
      implements Composite<OutputStream> {
   private final Collection<OutputStream> streams =
         new ArrayList<>();
   @Override
   public void add(OutputStream out) {
      streams.add(out);
   }
   @Override
   public boolean remove(OutputStream out) {
      return streams.remove(out);
   }
   @Override
   public void write(int b) throws IOException {
      for (var out : streams) {
         out.write(b);
      }
   }
   @Override
   public void flush() throws IOException {
      for (var out : streams) {
         out.flush();
      }
   }
   @Override
   public void close() throws IOException {
      for (var out : streams) {
         out.close();
      }
   }
}
// end::listing[]
