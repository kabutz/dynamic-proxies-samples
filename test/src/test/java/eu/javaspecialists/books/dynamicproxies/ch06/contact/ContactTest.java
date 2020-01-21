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

import java.util.function.*;

import static org.junit.Assert.*;

public class ContactTest {
  private static class CountingPerson extends Person {
    private int messages = 0;
    public CountingPerson(String email) {
      super(email);
    }
    @Override
    public void sendMail(String body) {
      super.sendMail(body);
      messages++;
    }
    public int getMessages() {
      return messages;
    }
  }
  protected void test(Supplier<Contact> compositeCreator) {
    Contact javaSpecialistsNewsletter = compositeCreator.get();
    assertEquals(0, javaSpecialistsNewsletter.count());
    var john = new CountingPerson("john@aol.com");
    assertEquals(0, john.getMessages());
    javaSpecialistsNewsletter.add(john);

    javaSpecialistsNewsletter.sendMail("Hello there 1");
    assertEquals(1, javaSpecialistsNewsletter.count());
    assertEquals(1, john.getMessages());

    Contact allStudents = compositeCreator.get();
    allStudents.add(new Person("peter@absa.co.za"));
    allStudents.add(new Person("mzani@absa.co.za"));
    javaSpecialistsNewsletter.add(allStudents);
    assertEquals(3, javaSpecialistsNewsletter.count());
    javaSpecialistsNewsletter.sendMail("Hello there 2");
    assertEquals(2, john.getMessages());

    Contact extremeJava = compositeCreator.get();
    extremeJava.add(new Person("John@standardbank.co.za"));
    extremeJava.add(new Person("Hlope@standardbank.co.za"));
    allStudents.add(extremeJava);
    assertEquals(5, javaSpecialistsNewsletter.count());
    javaSpecialistsNewsletter.sendMail("Hello there 3");
    assertEquals(3, john.getMessages());
  }
}