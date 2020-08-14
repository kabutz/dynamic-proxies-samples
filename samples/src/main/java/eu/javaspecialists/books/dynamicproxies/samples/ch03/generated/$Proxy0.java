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

package eu.javaspecialists.books.dynamicproxies.samples.ch03.generated;

// tag::listing[]
import eu.javaspecialists.books.dynamicproxies.samples.ch03.*;

import java.lang.reflect.*;
import java.text.*;
import java.time.*;

public final class $Proxy0 extends Proxy
    implements ISODateParser {
  private static Method m0;
  private static Method m1;
  private static Method m2;
  private static Method m3;

  public $Proxy0(InvocationHandler h) {
    super(h);
  }

  public final LocalDate parse(String s) throws ParseException {
    try {
      return (LocalDate) h.invoke(this, m3, new Object[] {s});
    } catch (RuntimeException | ParseException | Error e) {
      throw e;
    } catch (Throwable e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  public final int hashCode() {
    try {
      return (Integer) h.invoke(this, m0, (Object[]) null);
    } catch (RuntimeException | Error e) {
      throw e;
    } catch (Throwable e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  public final boolean equals(Object o) {
    try {
      return (Boolean) h.invoke(this, m1, new Object[] {o});
    } catch (RuntimeException | Error e) {
      throw e;
    } catch (Throwable e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  public final String toString() {
    try {
      return (String) h.invoke(this, m2, (Object[]) null);
    } catch (RuntimeException | Error e) {
      throw e;
    } catch (Throwable e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  static {
    try {
      m0 = Object.class.getMethod("hashCode");
      m1 = Object.class.getMethod("equals", Object.class);
      m2 = Object.class.getMethod("toString");
      m3 = ISODateParser.class.getMethod("parse", String.class);
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodError(e.getMessage());
    }
  }
}
// end::listing[]



