package eu.javaspecialists.books.dynamicproxies.ch02.remote;

import javax.xml.ws.*;

// tag::ServicePublisher[]
public class ServicePublisher {
    public static void main(String... args) {
        Endpoint.publish("http://localhost:9999/ws/service",
                new RealCanada());
    }
}
// end::ServicePublisher[]

