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

package eu.javaspecialists.books.dynamicproxies.ch07;

import java.io.*;

// tag::VirtualProxySourceGeneratorBasic[]
class VirtualProxySourceGeneratorBasic<P>
      extends VirtualProxySourceGenerator<P> {
   public VirtualProxySourceGeneratorBasic(
         Class<P> subject, Class<? extends P> realSubject) {
      super(subject, realSubject, Concurrency.NONE);
   }
   @Override
   protected void addRealSubjectCreation(PrintWriter out) {
      out.println(
            """
                private $name realSubject;
                private $name realSubject() {
                    if (realSubject == null) {
                        realSubject = new $realName();
                    }
                    return realSubject;
                }
                """.replace("$name", getSubjectName())
                  .replace("$realName", getRealSubjectName()));
   }
}
// end::VirtualProxySourceGeneratorBasic[]