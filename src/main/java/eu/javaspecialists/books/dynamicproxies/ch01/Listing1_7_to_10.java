package eu.javaspecialists.books.dynamicproxies.ch01;


import javax.jws.*;
import javax.jws.soap.*;
import javax.xml.namespace.*;
import javax.xml.ws.*;
import java.io.*;
import java.net.*;

public class Listing1_7_to_10 {
    // tag::Service[]
    @WebService
    @SOAPBinding(style = SOAPBinding.Style.RPC)
    public interface Service {
        @WebMethod
        public String work(String txt);
    }
    // end::Service[]

    static
    // tag::ServiceImpl[]
    @WebService(endpointInterface = "eu.javaspecialists.Service")
    public class ServiceImpl implements Service {
        @Override
        public String work(String txt) {
            System.out.println("remote txt = " + txt);
            return "ServiceImpl - " + txt;
        }
    }
    // end::ServiceImpl[]


    static
    // tag::ServicePublisher[]
    public class ServicePublisher {
        public static void main(String... args) {
            Endpoint.publish("http://localhost:9999/ws/service",
                new ServiceImpl());
        }
    }
    // end::ServicePublisher[]

    static

    // tag::ServiceRemoteProxy[]
    public class ServiceRemoteProxy implements Service {
        private final URL url;
        private final Service realSubject;
        public ServiceRemoteProxy() throws MalformedURLException {
            url = new URL("http://localhost:9999/ws/service?wsdl");
            var namespaceURI = "http://javaspecialists.eu/";
            var localPart = "ServiceImplService";
            var qname = new QName(namespaceURI, localPart);
            var service = javax.xml.ws.Service.create(url, qname);
            realSubject = service.getPort(Service.class);
        }
        public String work(String txt) {
            return realSubject.work(txt);
        }
    }
    // end::ServiceRemoteProxy[]

    static
    // tag::Main[]
    public class Main {
        public static void main(String... args) throws IOException {
            Service proxy = new ServiceRemoteProxy();
            System.out.println("proxy.work() = "
                + proxy.work("Hello"));
        }
    }
    // end::Main[]
}
