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

package eu.javaspecialists.books.dynamicproxies.ch03.benchmarks;

import java.lang.reflect.*;

public class ManualProxy extends Proxy implements Worker {
  private static Method m0;
  private static Method m1;
  private static Method m2;
  private static Method m3;
  private static Method m4;

  public ManualProxy(InvocationHandler invocationHandler) {
    super(invocationHandler);
  }

  @Override
  public final int hashCode() {
    try {
      return (Integer) this.h.invoke(this, m0, null);
    } catch (Error | RuntimeException throwable) {
      throw throwable;
    } catch (Throwable throwable) {
      throw new UndeclaredThrowableException(throwable);
    }
  }

  @Override
  public final boolean equals(Object object) {
    try {
      return (Boolean) this.h.invoke(this, m1,
          new Object[] {object});
    } catch (Error | RuntimeException throwable) {
      throw throwable;
    } catch (Throwable throwable) {
      throw new UndeclaredThrowableException(throwable);
    }
  }

  @Override
  public final String toString() {
    try {
      return (String) this.h.invoke(this, m2, null);
    } catch (Error | RuntimeException throwable) {
      throw throwable;
    } catch (Throwable throwable) {
      throw new UndeclaredThrowableException(throwable);
    }
  }

  @Override
  public final long increment() {
    try {
      return (long) this.h.invoke(this, m3, null);
    } catch (Error | RuntimeException throwable) {
      throw throwable;
    } catch (Throwable throwable) {
      throw new UndeclaredThrowableException(throwable);
    }
  }

  @Override
  public final void consumeCPU() {
    try {
      this.h.invoke(this, m4, null);
    } catch (Error | RuntimeException throwable) {
      throw throwable;
    } catch (Throwable throwable) {
      throw new UndeclaredThrowableException(throwable);
    }
  }

  static {
    try {
      m0 = Class.forName("java.lang.Object")
               .getMethod("hashCode");
      m1 = Class.forName("java.lang.Object")
               .getMethod("equals",
                   Class.forName("java.lang.Object"));
      m2 = Class.forName("java.lang.Object")
               .getMethod("toString");
      m3 = Class.forName(
          "eu.javaspecialists.books.dynamicproxies.ch03" +
              ".benchmarks.Worker").getMethod("increment");
      m4 = Class.forName(
          "eu.javaspecialists.books.dynamicproxies.ch03" +
              ".benchmarks.Worker").getMethod("consumeCPU");
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new NoSuchMethodError(noSuchMethodException.getMessage());
    } catch (ClassNotFoundException classNotFoundException) {
      throw new NoClassDefFoundError(classNotFoundException.getMessage());
    }
  }
}
