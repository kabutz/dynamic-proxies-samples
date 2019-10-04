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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap07_0;

import java.lang.reflect.Proxy;
import java.time.LocalDateTime;

/**
 * Created by Sven Ruppert on 14.04.2014.
 */
public class ProxyGenerator {

    //  public static <P> P makeComposite(Class<P> subintf, final P...
    //  realSubjects) {
    //    return subintf.cast(Proxy.newProxyInstance(
    //        subintf.getClassLoader(),
    //        new Class<?>[]{subintf},
    //        new InvocationHandler() {
    //          public Object invoke(Object proxy, Method method, Object[]
    //          args) throws Throwable {
    //            Object lastResult = null;
    //            for (P realSubject : realSubjects) {
    //              lastResult = method.invoke(realSubject, args);
    //            }
    //            return lastResult;
    //          }
    //        }
    //    ));
    //  }
    public static <P> P makeComposite(Class<P> subintf,
                                      final P... realSubjects) {
        return subintf.cast(Proxy.newProxyInstance(
            subintf.getClassLoader(),
            new Class<?>[]{subintf},
            (proxy, method, args) -> {
                Object lastResult = null;
                for (P realSubject : realSubjects) {
                    System.out.println("realSubject = " + realSubject.getClass());
                    lastResult = method.invoke(realSubject, args);
                }
                return lastResult;
            }
        ));
    }

    public static void main(String[] args) {

        Subject_A subject_a = new Subject_A();
        Subject_B subject_b = new Subject_B();
        Subject_C subject_c = new Subject_C();

        Subject composite = ProxyGenerator
            .makeComposite(Subject.class,
                subject_a, subject_b, subject_c);

        String doSomething = composite.doSomething(LocalDateTime.now());
        System.out.println("doSomething = " + doSomething);
    }

}
