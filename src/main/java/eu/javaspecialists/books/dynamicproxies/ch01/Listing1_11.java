package eu.javaspecialists.books.dynamicproxies.ch01;

public class Listing1_11 {
    // tag::Service[]
    public interface Service {
        public String work(String txt);
    }
    // end::Service[]
    static
    // tag::ServiceImpl[]
    public class ServiceImpl implements Service {
        @Override
        public String work(String txt) {
            return "ServiceImpl - " + txt;
        }
    }
    // end::ServiceImpl[]

    static
    // tag::ServiceSecurityProxy[]
    public class ServiceSecurityProxy implements Service {
        private Service service = new ServiceImpl();
        private String code = "";
        //Simmulation der Tastatureingabe
        public void setCode(String code) {
            this.code = code;
        }
        public String work(String txt) {
            if (code.equals("hoppel")) {
                return service.work(txt);
            } else {
                return "nooooop";
            }
        }
    }
    // end::ServiceSecurityProxy[]
    static
    // tag::Main[]
    public class Main {
        public static void main(String[] args) {
            Service proxy = new ServiceSecurityProxy();
            //Eingabe simulieren
            ((ServiceSecurityProxy) proxy).setCode("Nase");
            System.out.println(proxy.work("Hallo"));
            //Eingabe simulieren
            ((ServiceSecurityProxy) proxy).setCode("hoppel");
            System.out.println(proxy.work("Hallo"));
        }
    }
    // end::Main[]

}
