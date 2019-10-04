package eu.javaspecialists.books.dynamicproxies.ch02.remote;


import java.io.*;

// tag::Client[]
public class Client {
    public static void main(String... args) throws IOException {
        Canada proxy = new CanadianEmbassyInRome();
        boolean visa = proxy.canGetVisa("Heinz Kabutz", true, false);
        System.out.println("visa = " + visa);
    }
}
// end::Client[]

