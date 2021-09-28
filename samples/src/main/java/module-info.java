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
module eu.javaspecialists.books.dynamicproxies.samples {
  requires eu.javaspecialists.books.dynamicproxies;
  requires java.desktop;
  requires java.logging;
  requires java.net.http;
  requires spark.core;

  opens eu.javaspecialists.books.dynamicproxies.samples.ch02.virtual to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch03 to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch03.gotchas to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch04.immutablecollection to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch04.infiniterandom to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch05.bettercollection to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch05 to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch05.singer to
      eu.javaspecialists.books.dynamicproxies;
  opens eu.javaspecialists.books.dynamicproxies.samples.ch06.contactdynamic to
      eu.javaspecialists.books.dynamicproxies;
}
