# Dynamic Proxies Samples

This repository is a companion to the book
[Dynamic Proxies in Java](https://www.infoq.com/minibooks/java-dynamic-proxies/)
by Dr Heinz M. Kabutz.

It contains several modules.

1. The `core` module is a library of dynamic proxy helper
   classes. The `Proxies` class is a façade that makes it
   easy to use the common configurations. The classes in this
   library are described in the book and used in the other modules.
   The core is published in the
   [Maven Central Repository](https://search.maven.org/artifact/eu.javaspecialists.books.dynamicproxies/core)
   and you may use it in your projects under the
   [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
   The coordinates are `eu.javaspecialists.books.dynamicproxies:core:2.0.0`,
   or add this dependency to your pom.xml:
   ```
   <dependency>
       <groupId>eu.javaspecialists.books.dynamicproxies</groupId>
       <artifactId>core</artifactId>
       <version>2.0.0</version>
   </dependency>
   ```

2. The `samples` module is a collection of the sample code used
   in the book.

3. The `benchmarks` module is a set of benchmarks using the
   [Java Microbenchmark Harness](https://openjdk.java.net/projects/code-tools/jmh/)
   (JMH) to test the performance of dynamic proxies. The results
   of the benchmarks are summarized in the book.


## Benchmarks

To exercise the benchmarks, run the following in a terminal, or use your IDE to do the equivalent:

`mvn verify -P run-benchmarks` 

## Java Platform Module System (JPMS)

The `core` and `samples` modules use JPMS so that we can show the
effects of JPMS modules on dynamic proxies, and avoid the illegal
access warnings from the JRE when running the code.

For the `benchmarks` module, however, there is a package clash 
in JMH v1.21 which blocks JPMS from loading the components of
JMH as automatic modules.
In particular, the package `org.openjdk.jmh.generators.core` in
`jmh-core` clashes with the package `org.openjdk.jmh.generators`
in `jmh-generator-annprocess`.
 