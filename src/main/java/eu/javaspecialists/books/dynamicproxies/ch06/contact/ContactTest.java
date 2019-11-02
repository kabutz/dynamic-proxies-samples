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

public class ContactTest {
  public static void main(String... args) {
    // tag::listing[]
    Contact java_specialists_newsletter = new DistributionList();
    System.out.println(java_specialists_newsletter.count());
    java_specialists_newsletter.add(new Person("john@aol.com"));
    java_specialists_newsletter.sendMail("Hello there 1");
    System.out.println(java_specialists_newsletter.count());

    DistributionList all_students = new DistributionList();
    java_specialists_newsletter.add(all_students);
    all_students.add(new Person("peter@absa.co.za"));
    all_students.add(new Person("mzani@absa.co.za"));

    java_specialists_newsletter.sendMail("Hello there 2");
    System.out.println(java_specialists_newsletter.count());

    Contact extreme_java_students = new DistributionList();
    all_students.add(extreme_java_students);
    extreme_java_students.add(new Person("John@fnb.co.za"));
    extreme_java_students.add(new Person("Hlope@fnb.co.za"));

    java_specialists_newsletter.sendMail("Hello there 3");
    System.out.println(java_specialists_newsletter.count());
    // end::listing[]
  }
}
