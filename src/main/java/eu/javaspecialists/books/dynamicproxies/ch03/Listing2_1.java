package eu.javaspecialists.books.dynamicproxies.ch03;

import java.lang.reflect.Proxy;

public class Listing2_1 {
    // tag::Subject[]
    public interface Subject {
        String doSomething(String str);
    }
    // end::Subject[]
    static
    // tag::RealSubject[]
    public class RealSubject implements Subject {
        @Override
        public String doSomething(String str) {
            System.out.println("str = " + str);
            return str + "_DONE";
        }
        public String toString() {
            return "RealSubject";
        }
    }
    // end::RealSubject[]
    static
    // tag::Proxies[]
    public class Proxies { // Façade Design Pattern
        private Proxies() {}
        public static <P> P makeSimpleProxy(Class<P> clazz, P p) {
            return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                (proxy, method, args) -> method.invoke(p, args)
            ));
        }
    }
    // end::Proxies[]
    static
    public class Main {
        public static void main(String... args) {
            // tag::main()[]
            Subject subject = Proxies
                .makeSimpleProxy(Subject.class, new RealSubject());
            String hello = subject.doSomething("Hello");
            System.out.println("hello = " + hello);
            System.out.println(subject);
            System.out.println(subject.getClass());

            // Output:
            //str = Hello
            //hello = Hello_DONE
            //RealSubject
            //class com.sun.proxy.$Proxy0
            // end::main()[]
        }
    }
}
