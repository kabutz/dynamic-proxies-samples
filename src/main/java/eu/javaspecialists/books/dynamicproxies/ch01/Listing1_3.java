package eu.javaspecialists.books.dynamicproxies.ch01;

public class Listing1_3 {
    public static class Proxy {
        // tag::listing1[]
        // Proxy
        public interface A {
            public void f();
        }
        public class B implements A {
            public void f() { /* do something */ }
        }
        public class C implements A {
            private A a;
            public C(A a) { this.a = a; }
            public void f() {
                /* do something, then */
                a.f();
            }
        }
        // end::listing1[]
    }
    public static class Decorator {
        // tag::listing2[]
        // Decorator
        public interface A {
            public void f();
        }
        public class B implements A {
            public void f() { /* do something */ }
        }
        public class C implements A {
            private A a;
            public C(A a) { this.a = a; }
            public void f() {
                /* do something, then */
                a.f();
            }
        }
        class D extends C {
            public D(A a) { super(a); }
            public void g() {
                /* decorate in some way, then call f() */
                f();
            }
        }
        // end::listing2[]
    }
}
