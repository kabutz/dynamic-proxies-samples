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

package eu.javaspecialists.books.dynamicproxies.ch06.contact;

public class ContactTest {
    public static void main(String... args) {
        // tag::listing[]
        Contact tjsn = new DistributionList();
        System.out.println(tjsn.count());
        tjsn.add(new Person("john@aol.com"));
        tjsn.sendMail("Hello there 1");
        System.out.println(tjsn.count());

        DistributionList students = new DistributionList();
        tjsn.add(students);
        students.add(new Person("peter@absa.co.za"));
        students.add(new Person("mzani@absa.co.za"));

        tjsn.sendMail("Hello there 2");
        System.out.println(tjsn.count());

        Contact extreme_java = new DistributionList();
        extreme_java.add(new Person("John@standardbank.co.za"));
        extreme_java.add(new Person("Hlope@standardbank.co.za"));
        students.add(extreme_java);

        tjsn.sendMail("Hello there 3");
        System.out.println(tjsn.count());
        // end::listing[]
    }
}
