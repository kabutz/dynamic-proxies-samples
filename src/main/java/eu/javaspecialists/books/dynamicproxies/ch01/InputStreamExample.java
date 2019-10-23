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

package eu.javaspecialists.books.dynamicproxies.ch01;

import java.io.*;
import java.util.concurrent.*;
import java.util.zip.*;

public class InputStreamExample {
    public static void main(String... args) throws IOException {
        long time = System.currentTimeMillis();

        // tag::out[]
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(
                        new GZIPOutputStream(
                                new FileOutputStream("data.bin.gz"))))) {
            ThreadLocalRandom.current().ints(10_000_000, 0, 1000)
                    .forEach(i -> {
                        try {
                            out.writeInt(i);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
        // end::out[]

        time = System.currentTimeMillis() - time;
        System.out.println("Finished writing in " + time + " ms");
        time = System.currentTimeMillis();

        // tag::in[]
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        new GZIPInputStream(
                                new FileInputStream("data.bin.gz"))))) {
            long total = 0;
            while (in.available() > 0) {
                total += in.readInt();
            }
            System.out.println("total = " + total);
        }
        // end::in[]

        time = System.currentTimeMillis() - time;
        System.out.println("Finished reading in " + time + " ms");
    }
}
