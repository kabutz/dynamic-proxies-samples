package eu.javaspecialists.books.dynamicproxies.ch01;

public class Listing1_2 {
    // tag::listing[]
    public interface Component {
        public void operation();
    }

    public class ConcreteComponent implements Component {
        public void operation() { /* do something */ }
    }

    public class Decorator implements Component {
        private Component component;

        public Decorator(Component component) {
            this.component = component;
        }

        public void operation() {
            /* do something, then */
            component.operation();
        }
    }

    public class ConcreteDecorator extends Decorator {
        public ConcreteDecorator(Component component) {
            super(component);
        }

        public void anotherOperation() {
            /* decorate in some way, then call operation() */
            operation();
        }
    }
    // end::listing[]
}
