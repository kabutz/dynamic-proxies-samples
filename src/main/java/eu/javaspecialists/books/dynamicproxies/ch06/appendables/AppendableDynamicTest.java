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

import eu.javaspecialists.books.dynamicproxies.*;

import java.io.*;

public class AppendableDynamicTest {
    public static void main(String... args) throws IOException {
        // tag::listing[]
        var tee = Proxies.compose(AppendableCloseableFlushable.class);
        StringWriter sw = new StringWriter();
        tee.add(new OutputStreamWriter(System.out));
        tee.add(new FileWriter("output.txt"));
        tee.add(sw);

        var out = new PrintWriter(new WriterAdapter<>(tee));
        out.println("Hello World Dynamic");
        out.flush();

        tee.append("TestingAppenderDynamic")
                .append('\n')
                .append("Does this work?")
                .append('\n');
        tee.flush();

        System.out.println("sw = " + sw);
        // end::listing[]
    }
}
