/*
 * Copyright (C) 2020 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz
 * licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */

package eu.javaspecialists.books.dynamicproxies.chapters.ch01;

public class ChainOfResponsibilityStructure {
  // tag::listing[]
  public class Client {
    private final Handler handler;
    public Client(Handler handler) {
      this.handler = handler;
    }
    public void execute() {
      handler.handleRequest();
    }
  }
  public abstract class Handler {
    private final Handler next;
    public Handler(Handler next) {
      this.next = next;
    }
    public void handleRequest() {
      if (next != null) next.handleRequest();
    }
  }
  public class ConcreteHandler1 extends Handler {
    public ConcreteHandler1(Handler next) {
      super(next);
    }
    public void handleRequest() {
      /* left to our imagination */
      super.handleRequest(); // pass on to next handler in chain
    }
  }
  public class ConcreteHandler2 extends Handler {
    public ConcreteHandler2(Handler next) {
      super(next);
    }
    public void handleRequest() {
      /* left to our imagination */
      // do not pass on to next handler in chain
    }
  }
  // end::listing[]
}
