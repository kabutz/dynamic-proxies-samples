package eu.javaspecialists.books.dynamicproxies.ch01;

public class ProxyInJava {
    // tag::listing[]
    public interface Subject {
        public void request();
    }

    public class RealSubject implements Subject {
        public void request() { /* do something */ }
    }

    public class Proxy implements Subject {
        private Subject realSubject;

        public Proxy(Subject realSubject) {
            this.realSubject = realSubject;
        }

        public void request() {
            /* do something, then */
            realSubject.request();
        }
    }
    // end::listing[]
}
