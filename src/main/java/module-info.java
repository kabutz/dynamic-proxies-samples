module eu.javaspecialists.books.dynamicproxies {
  requires java.net.http;
  requires java.logging;
  requires java.desktop;
  requires spark.core;
  requires java.sql; // used by test

  exports eu.javaspecialists.books.dynamicproxies;
  exports eu.javaspecialists.books.dynamicproxies.util;
  exports eu.javaspecialists.books.dynamicproxies.util.chain;

  // please let us know if we need to export more by emailing
  // heinz@javaspecialists.eu
}