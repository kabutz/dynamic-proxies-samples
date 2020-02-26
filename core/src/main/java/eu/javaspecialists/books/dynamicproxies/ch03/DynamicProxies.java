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
import java.time.*;

public class DynamicProxies {
  public static void main(String... args) throws ParseException {
    // tag::listing[]
    RealISODateParser realParser = new RealISODateParser();
    ISODateParser parser = Proxies.simpleProxy(
        ISODateParser.class, realParser);
    LocalDate palindrome = parser.parse("2020-02-02");
    System.out.println("palindrome = \"" + palindrome + "\"");
    System.out.println(parser);

    System.out.println(realParser.equals(parser));
    System.out.println(parser.equals(realParser));

    LocalDate funnyDate = parser.parse("2020-04-31");
    // end::listing[]
  }
}
