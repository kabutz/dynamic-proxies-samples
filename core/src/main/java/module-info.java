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
module eu.javaspecialists.books.dynamicproxies {
  requires java.net.http;
  requires java.logging;
  requires java.desktop;
  requires spark.core;

  exports eu.javaspecialists.books.dynamicproxies;
  exports eu.javaspecialists.books.dynamicproxies.util;
  exports eu.javaspecialists.books.dynamicproxies.util.chain;
  exports eu.javaspecialists.books.dynamicproxies.ch02.equals;
  exports eu.javaspecialists.books.dynamicproxies.ch02.protection;
  exports eu.javaspecialists.books.dynamicproxies.ch03.enhancedstream;
  exports eu.javaspecialists.books.dynamicproxies.ch03.gotchas;
  exports eu.javaspecialists.books.dynamicproxies.ch03.protection;
  exports eu.javaspecialists.books.dynamicproxies.ch03.virtual;
  exports eu.javaspecialists.books.dynamicproxies.ch04;
  exports eu.javaspecialists.books.dynamicproxies.ch04.immutablecollection;
  exports eu.javaspecialists.books.dynamicproxies.ch05;
  exports eu.javaspecialists.books.dynamicproxies.ch05.bettercollection;
  exports eu.javaspecialists.books.dynamicproxies.ch06;
  exports eu.javaspecialists.books.dynamicproxies.ch06.appendables;
  exports eu.javaspecialists.books.dynamicproxies.ch06.contact;

  // please let us know if we need to export more by emailing
  // heinz@javaspecialists.eu
}