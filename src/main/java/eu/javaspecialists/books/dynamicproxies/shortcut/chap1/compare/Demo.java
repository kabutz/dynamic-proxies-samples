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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap1.compare;

import java.util.Date;

/**
 * Created by Sven Ruppert on 04.01.14.
 */
public class Demo {
    interface Subject {
        public void request();
    }

    class RealSubject implements Subject {
        public void request() {
            System.out.println("RealSubject.request(); = "
                + new Date());
        }
    }

    class Proxy implements Subject {
        private Subject realSubject;

        Proxy(Subject realSubject) {
            this.realSubject = realSubject;
        }

        public void request() {
            /* do something, then */
            realSubject.request();
        }
    }

    public static void main(String[] args) {
        Demo main = new Demo();
        main.new Proxy(main.new RealSubject()).request();

        main.new ConcreteDecorator(main.new ConcreteComponent()).operation();

    }


    interface Component {
        public void operation();
    }

    class ConcreteComponent implements Component {
        public void operation() {
            System.out.println("ConcreteComponent.operation() " + new Date());
        }
    }

    abstract class Decorator implements Component {
        private Component component;

        Decorator(Component component) {
            this.component = component;
        }

        public void operation() {
            /* do something, then */
            component.operation();
        }
    }

    class ConcreteDecorator extends Decorator {
        ConcreteDecorator(Component component) {
            super(component);
        }

        public void anotherOperation() {
        /* decorate the other operation in some way, then call the
        other operation() */
            operation();
        }

    }

}
