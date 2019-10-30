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
public class TeeAppendable<E extends Appendable & Closeable & Flushable>
      implements Appendable, Closeable, Flushable, Composite<E> {
   private final Collection<E> children = new ArrayList<>();

   @Override
   public void add(E child) {
      children.add(child);
   }

   @Override
   public boolean remove(E child) {
      return children.remove(child);
   }

   @Override
   public Appendable append(CharSequence csq) throws IOException {
      for (var child : children) child.append(csq);
      return this;
   }
   @Override
   public Appendable append(CharSequence csq, int start, int end)
         throws IOException {
      for (var child : children) child.append(csq, start, end);
      return this;
   }
   @Override
   public Appendable append(char c) throws IOException {
      for (var child : children) child.append(c);
      return this;
   }
   @Override
   public void close() throws IOException {
      for (var child : children) child.close();
   }
   @Override
   public void flush() throws IOException {
      for (var child : children) child.flush();
   }
}
// end::listing[]