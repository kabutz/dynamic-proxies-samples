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
import org.junit.*;

import java.lang.reflect.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.stream.*;

import static org.junit.Assert.*;

public class ISODateParserTest {
  private final ISODateParser[] parsers = {
      Proxies.simpleProxy(ISODateParser.class,
          new RealISODateParser()),
      Proxies.simpleProxy(ISODateParser.class,
          new FastISODateParser()),
      new RealISODateParser(),
      new FastISODateParser(),
  };

  private static volatile Object leak;

  @Test
  public void testNullDate() throws ParseException {
    for (ISODateParser parser : parsers) {
      try {
        parser.parse(null);
        fail("Expected NullPointerException");
      } catch (NullPointerException success) {
      }
    }
  }

  @Test
  public void testInvalidDate() {
    checkBadDate("2020.02-02");
    checkBadDate("2020-02.02");
    checkBadDate("02.02.2020");
    checkBadDate("02-02-2020");
    checkBadDate("2020-0a-02");
    checkBadDate("2020-02-30");
  }

  private void checkBadDate(String date) {
    for (ISODateParser parser : parsers) {
      try {
        parser.parse(date);
        fail("Expected ParseException for date=" + date);
      } catch (ParseException success) {
      }
    }
  }

  @Test
  public void testCorrectDates() throws ParseException {
    LocalDate date = LocalDate.of(2020, Month.JANUARY, 1);
    for (int i = 0; i < 366; i++) {
      String text =
          DateTimeFormatter.ISO_LOCAL_DATE.format(date);
      for (ISODateParser parser : parsers) {
        assertEquals(LocalDate.parse(text,
            DateTimeFormatter.ISO_LOCAL_DATE),
            leak = parser.parse(text));
      }
      date = date.plusDays(1);
    }
    for (ISODateParser parser : parsers) {
      assertEquals(LocalDate.of(1987, Month.DECEMBER, 13),
          parser.parse("1987-12-13"));
    }
  }

  @Test
  public void testThreadSafety() throws ParseException {
    LocalDate date = LocalDate.of(2020, Month.FEBRUARY, 2);
    for (ISODateParser parser : parsers) {
      if (parser instanceof Proxy) {
        System.out.print("Dynamic Proxy of ");
      }
      System.out.println(parser);
      long time = System.nanoTime();
      try {
        IntStream.range(0, 10_000_000)
            .parallel()
            .forEach(i -> {
              try {
                assertEquals(date, parser.parse("2020-02-02"));
              } catch (ParseException e) {
                throw new AssertionError(e);
              }
            });
      } finally {
        time = System.nanoTime() - time;
        System.out.printf("time = %dms%n", (time / 1_000_000));
      }
    }
  }
}