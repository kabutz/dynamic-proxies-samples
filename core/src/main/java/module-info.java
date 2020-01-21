module eu.javaspecialists.books.dynamicproxies {
  requires java.net.http;
  requires java.logging;
  requires java.desktop;
  requires spark.core;
  requires java.sql; // used by test

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