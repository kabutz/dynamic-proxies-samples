/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
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

package eu.javaspecialists.books.dynamicproxies.ch06.contact;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.ch06.*;

import java.util.*;

public class ContactDynamicTest {
  public static void main(String... args) {
    // tag::listing[]
    var reducers = Map.of(
        new MethodKey(Contact.class, "count"),
        new Reducer(0, (r1, r2) -> (int) r1 + (int) r2)
    );

    Contact javaSpecialistsNewsletter =
        Proxies.compose(Contact.class, reducers);
    System.out.println(javaSpecialistsNewsletter.count());
    javaSpecialistsNewsletter.add(new Person("john@aol.com"));
    javaSpecialistsNewsletter.sendMail("Hello there 1");
    System.out.println(javaSpecialistsNewsletter.count());

    Contact allStudents =
        Proxies.compose(Contact.class, reducers);
    allStudents.add(new Person("peter@absa.co.za"));
    allStudents.add(new Person("mzani@absa.co.za"));
    javaSpecialistsNewsletter.add(allStudents);

    javaSpecialistsNewsletter.sendMail("Hello there 2");
    System.out.println(javaSpecialistsNewsletter.count());

    Contact extremeJava =
        Proxies.compose(Contact.class, reducers);
    extremeJava.add(new Person("John@standardbank.co.za"));
    extremeJava.add(new Person("Hlope@standardbank.co.za"));
    allStudents.add(extremeJava);

    javaSpecialistsNewsletter.sendMail("Hello there 3");
    System.out.println(javaSpecialistsNewsletter.count());
    // end::listing[]
  }
}