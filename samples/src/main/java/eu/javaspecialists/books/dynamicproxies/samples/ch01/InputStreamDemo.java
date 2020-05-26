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

package eu.javaspecialists.books.dynamicproxies.samples.ch01;

import java.io.*;
import java.util.concurrent.*;
import java.util.zip.*;

public class InputStreamDemo {
  public static void main(String... args) throws IOException {
    long time = System.currentTimeMillis();

    // tag::out[]
    try (var out = new DataOutputStream(
        new BufferedOutputStream(
            new GZIPOutputStream(
                new FileOutputStream(
                    "data.bin.gz"))))) {
      // write ten million random ints between 0 and 1000 to
      // data.bin.gz, compressing the file with GZIP on the fly
      ThreadLocalRandom.current().ints(10_000_000, 0, 1_000)
          .forEach(i -> {
            try {
              out.writeInt(i);
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          });
      out.writeInt(-1); // our EOF marker
    }
    // end::out[]

    time = System.currentTimeMillis() - time;
    System.out.println("Finished writing in " + time + " ms");
    time = System.currentTimeMillis();

    // tag::in[]
    try (
        // declared "fis" separately to ensure it is closed, even
        // if the file does not contain a proper GZIP header
        var fis = new FileInputStream("data.bin.gz");
        var in = new DataInputStream(
            new BufferedInputStream(
                new GZIPInputStream(fis)))) {
      long total = 0;
      int value;
      // keep reading until value == -1, our EOF marker
      while ((value = in.readInt()) != -1) {
        total += value;
      }
      // we expect total to be approximately 5 billion
      System.out.println("total = " + total);
    }
    // end::in[]

    time = System.currentTimeMillis() - time;
    System.out.println("Finished reading in " + time + " ms");
  }
}
