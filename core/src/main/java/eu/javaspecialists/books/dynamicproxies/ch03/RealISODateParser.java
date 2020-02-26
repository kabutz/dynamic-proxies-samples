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

import java.text.*;
import java.time.*;

// tag::listing[]
public final class RealISODateParser implements ISODateParser {
  // Super fast date ISO parser that does not allocate memory
  @Override
  public LocalDate parse(String date) throws ParseException {
    if (date == null) throw new NullPointerException();
    if (date.length() != 10)
      throw new ParseException("Bad length", date.length());
    if (date.charAt(4) != '-')
      throw new ParseException("Bad format", 4);
    if (date.charAt(7) != '-')
      throw new ParseException("Bad format", 7);
    int year = atoi(date, 0, 4);
    int month = atoi(date, 5, 2);
    int day = atoi(date, 8, 2);
    try {
      return LocalDate.of(year, month, day);
    } catch (DateTimeException e) {
      throw new ParseException(e.toString(), 0);
    }
  }
  // parse String as decimal and convert to int
  private int atoi(String s, int from, int length)
      throws ParseException {
    int result = 0;
    for (int i = from; i < from + length; i++) {
      result *= 10;
      result += atoi(s, i);
    }
    return result;
  }
  private int atoi(String s, int index) throws ParseException {
    int val = s.charAt(index) - '0';
    if (val < 0 || val > 9)
      throw new ParseException("Bad digit", index);
    return val;
  }

  @Override
  public String toString() {
    return "RealISODateParser";
  }
}
// end::listing[]