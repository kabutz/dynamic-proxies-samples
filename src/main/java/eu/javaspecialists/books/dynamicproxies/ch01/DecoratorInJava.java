/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.ch01;

public class DecoratorInJava {
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
