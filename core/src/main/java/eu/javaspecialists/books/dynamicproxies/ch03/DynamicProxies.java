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

package eu.javaspecialists.books.dynamicproxies.ch03;

import eu.javaspecialists.books.dynamicproxies.*;

import java.text.*;

public class DynamicProxies {
  public static void main(String... args) throws ParseException {
    // tag::listing[]
    RealSubject realSubject = new RealSubject(42);
    Subject subject = Proxies.simpleProxy(
        Subject.class, realSubject);
    String hello = subject.uppercaseTrim("  Hello\t\t");
    System.out.println("hello = \"" + hello + "\"");
    System.out.println(subject);

    System.out.println(realSubject.equals(subject));
    System.out.println(subject.equals(realSubject));
    // end::listing[]
  }
}
