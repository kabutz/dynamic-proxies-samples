package eu.javaspecialists.books.dynamicproxies.ch02.remote;

import javax.jws.*;

// tag::RealCanada[]
@WebService(endpointInterface = "eu.javaspecialists.books.dynamicproxies" +
                                        ".ch01.remote.Canada")
public class RealCanada implements Canada {
    public boolean canGetVisa(String name, boolean married, boolean rich) {
        System.out.println("Can get visa from Canada?");
        System.out.printf("name = %s, married = %s, rich = %s%n",
                name, married, rich);
        return married && rich || !married && rich;
    }
}
// end::RealCanada[]

