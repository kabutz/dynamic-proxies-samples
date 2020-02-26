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
import java.time.format.*;

// tag::listing[]
public final class RealISODateParser implements ISODateParser {
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ISO_LOCAL_DATE;
  @Override
  public LocalDate parse(String date) throws ParseException {
    try {
      return LocalDate.parse(date, formatter);
    } catch (DateTimeParseException e) {
      throw new ParseException(e.toString(), e.getErrorIndex());
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
// end::listing[]