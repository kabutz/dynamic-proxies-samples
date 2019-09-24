package eu.javaspecialists.books.dynamicproxies.ch01;

public class Listing1_4_to_6 {
    // tag::worker[]
    public interface Worker {
        public void doSomething();
    }
    // end::worker[]
    static
    // tag::workerimpl[]
    public class WorkerImpl implements Worker {
        public WorkerImpl() {
            System.out.println("WorkerImpl = constructed");
        }
        @Override
        public void doSomething() {
            System.out.println("doSomething => WorkerImpl");
        }
    }
    // end::workerimpl[]
    static
    // tag::virtualworker[]
    public abstract class VirtualWorker implements Worker {
        protected abstract Worker realSubject();
        @Override
        public void doSomething() {
            realSubject().doSomething();
        }
    }
    // end::virtualworker[]

    static
    // tag::virtualworkernotthreadsafe[]
    public class VirtualWorkerNotThreadSafe extends VirtualWorker {
        private Worker subject;
        @Override
        protected Worker realSubject() {
            if (subject == null) {
                subject = new WorkerImpl();
            }
            return subject;
        }
    }
    // end::virtualworkernotthreadsafe[]

    static
    // tag::main[]
    public class Main {
        public static void main(String... args) {
            Worker service = new VirtualWorkerNotThreadSafe();
            service.doSomething();
        }
    }
    // end::main[]

}
