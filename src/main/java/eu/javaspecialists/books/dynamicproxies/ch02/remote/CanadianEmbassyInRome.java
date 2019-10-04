package eu.javaspecialists.books.dynamicproxies.ch02.remote;

import javax.xml.namespace.*;
import javax.xml.ws.*;
import java.net.*;

// tag::CanadianEmbassyInRome[]
public class CanadianEmbassyInRome implements Canada {
    private final URL url;
    private final Canada realSubject;
    public CanadianEmbassyInRome() throws MalformedURLException {
        url = new URL("http://localhost:9999/ws/service?wsdl");
        var namespaceURI = "http://remote.ch01.dynamicproxies.books." +
                                   "javaspecialists.eu/";
        var localPart = "RealCanadaService";
        var qname = new QName(namespaceURI, localPart);
        var service = Service.create(url, qname);
        realSubject = service.getPort(Canada.class);
    }
    @Override
    public boolean canGetVisa(String name, boolean married, boolean rich) {
        return realSubject.canGetVisa(name, married, rich);
    }
}
// end::CanadianEmbassyInRome[]
