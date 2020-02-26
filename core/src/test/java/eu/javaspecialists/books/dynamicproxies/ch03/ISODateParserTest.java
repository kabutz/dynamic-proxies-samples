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

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.stream.*;

import static org.junit.Assert.*;

public class ISODateParserTest {
  private final ISODateParser parser =
      Proxies.simpleProxy(ISODateParser.class,
          new RealISODateParser());
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
    try {
      parser.parse(date);
      fail("Expected ParseException for date=" + date);
    } catch (ParseException success) {
    }
  }

  @Test
  public void testCorrectDates() throws ParseException {
    LocalDate date = LocalDate.of(2020, Month.JANUARY, 1);
    for (int i = 0; i < 366; i++) {
      String text = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
      assertEquals(LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE),
          parser.parse(text));
      date = date.plusDays(1);
    }
  }

  @Test
  public void testThreadSafety() throws ParseException {
    LocalDate date = LocalDate.of(2020, Month.FEBRUARY, 2);
    IntStream.range(0, 10_000_000)
        .parallel()
        .forEach(i -> {
          try {
            assertEquals(date, parser.parse("2020-02-02"));
          } catch (ParseException e) {
            throw new AssertionError(e);
          }
        });

  }
}