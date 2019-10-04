package eu.javaspecialists.books.dynamicproxies.ch02.remote;

import javax.jws.*;
import javax.jws.soap.*;

// tag::Canada[]
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface Canada {
    @WebMethod
    boolean canGetVisa(String name, boolean married, boolean rich);
}
// end::Canada[]
