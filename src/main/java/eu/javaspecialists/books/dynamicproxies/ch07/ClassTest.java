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

import javax.tools.*;
import java.net.*;
import java.util.*;

public class ClassTest {
    public static void main(String... args) throws IllegalAccessException, InstantiationException {

        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        if (jc == null) {
            throw new UnsupportedOperationException(
                    "Cannot find java compiler! " + "Probably only JRE " +
                            "installed.");
        }

        GeneratedClassFile gcf = new GeneratedClassFile();
        DiagnosticCollector<JavaFileObject> dc =
                new DiagnosticCollector<>();
        String watchThis = """
               package coolthings;

               public class WatchThis implements Runnable {
                 public WatchThis() {
                   System.out.println("Hey this works!");
                 }

                 public void run() {
                   System.out.println(Thread.currentThread());
                   while(Math.random() < 0.95) {
                     System.out.println("Cool stuff!");
                   }
                 }
               }
               """;
        GeneratedJavaSourceFile gjsf
                = new GeneratedJavaSourceFile("WatchThis", watchThis);
        GeneratingJavaFileManager fileManager =
                new GeneratingJavaFileManager(
                        jc.getStandardFileManager(dc, null, null), gcf);
        JavaCompiler.CompilationTask task = jc.getTask(
                null, fileManager, dc, null, null, Arrays.asList(gjsf));
        boolean result = task.call();
        Class<?> res;
        if (result) {
            try {
                byte[] data = gcf.getClassAsBytes();
                ProxyClassLoader cl = new ProxyClassLoader();
                res = cl.defineClass("coolthings.WatchThis", data);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("Proxy problem", e);
            }
        } else { // use your logging system of choice here
            System.err.println("Compile failed:");
            System.err.println((CharSequence) watchThis);
            for (Diagnostic<?> d :
                    ((DiagnosticCollector<?>) dc).getDiagnostics()) {
                System.err.println(d);
            }
            throw new IllegalArgumentException(
                    "Could not create proxy - compile failed");
        }
        Class<?> testClass = res;
        Runnable r = (Runnable) testClass.newInstance();
        Class<? extends Runnable> clazz = r.getClass();
        System.out.println("Our class: " + clazz.getName());
        System.out.println("Classloader: " + clazz.getClassLoader());
        Thread t = new Thread(r, "Cool Thread");
        t.start();

    }

    private static class ProxyClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b)
                throws ClassFormatError
        {
            return defineClass(name, b, 0, b.length, null);
        }
    }
}
