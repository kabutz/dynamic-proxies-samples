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

package eu.javaspecialists.books.dynamicproxies.chapters.ch06;

import eu.javaspecialists.books.dynamicproxies.*;
import eu.javaspecialists.books.dynamicproxies.handlers.*;

import javax.tools.*;
import java.io.*;
import java.lang.ref.*;
import java.net.*;
import java.util.*;

import static org.junit.Assert.*;

public class MemoryLeakPrevention {
  static class GeneratedJavaSourceFile extends SimpleJavaFileObject {
    private CharSequence javaSource;

    public GeneratedJavaSourceFile(String className,
                                   CharSequence javaSource) {
      super(URI.create(className + ".java"), Kind.SOURCE);
      this.javaSource = javaSource;
    }

    public CharSequence getCharContent(boolean ignoreEncodeErrors)
        throws IOException {
      return javaSource;
    }
  }

  static class GeneratedClassFile extends SimpleJavaFileObject {
    private final ByteArrayOutputStream outputStream =
        new ByteArrayOutputStream();

    public GeneratedClassFile() {
      super(URI.create("generated.class"), Kind.CLASS);
    }

    public OutputStream openOutputStream() {
      return outputStream;
    }

    public byte[] getClassAsBytes() {
      return outputStream.toByteArray();
    }
  }

  static class GeneratingJavaFileManager extends
      ForwardingJavaFileManager<JavaFileManager> {
    private final GeneratedClassFile gcf;

    public GeneratingJavaFileManager(
        StandardJavaFileManager sjfm, GeneratedClassFile gcf) {
      super(sjfm);
      this.gcf = gcf;
    }

    public JavaFileObject getJavaFileForOutput(
        Location location, String className,
        JavaFileObject.Kind kind, FileObject sibling)
        throws IOException {
      return gcf;
    }
  }

  public static class Generator extends ClassLoader {
    private static final JavaCompiler jc;

    static {
      jc = ToolProvider.getSystemJavaCompiler();
      if (jc == null) {
        throw new UnsupportedOperationException(
            "Cannot find java compiler!  " +
                "Probably only JRE installed.");
      }
    }
    public Generator(String name, ClassLoader parent) {
      super(name, parent);
    }

    public Class make(String className,
                      CharSequence javaSource) {
      GeneratedClassFile gcf = new GeneratedClassFile();

      DiagnosticCollector<JavaFileObject> dc =
          new DiagnosticCollector<JavaFileObject>();

      boolean result = compile(className, javaSource, gcf, dc);
      return processResults(className, javaSource, gcf, dc,
          result);
    }

    private boolean compile(
        String className, CharSequence javaSource,
        GeneratedClassFile gcf,
        DiagnosticCollector<JavaFileObject> dc) {
      GeneratedJavaSourceFile gjsf = new GeneratedJavaSourceFile(
          className, javaSource
      );
      GeneratingJavaFileManager fileManager =
          new GeneratingJavaFileManager(
              jc.getStandardFileManager(dc, null, null), gcf);
      JavaCompiler.CompilationTask task = jc.getTask(
          null, fileManager, dc, null, null,
          Arrays.asList(gjsf));
      return task.call();
    }

    private Class processResults(
        String className, CharSequence javaSource,
        GeneratedClassFile gcf, DiagnosticCollector<?> dc,
        boolean result) {
      if (result) {
        return createClass(className, gcf);
      } else {
        // use your logging system of choice here
        System.err.println("Compile failed:");
        System.err.println(javaSource);
        for (Diagnostic<?> d : dc.getDiagnostics()) {
          System.err.println(d);
        }
        throw new IllegalArgumentException(
            "Could not create proxy - compile failed");
      }
    }

    private Class createClass(String className,
                              GeneratedClassFile gcf) {
      byte[] data = gcf.getClassAsBytes();
      return defineClass(className, data, 0, data.length);
    }
  }

  private static WeakReference<Class<?>> makeACompositeAndUseIt(Class clazz) {
    BaseComponent composite = Proxies.compose(clazz);
    Object child = Proxies.compose(clazz);
    composite.add(child);

    System.out.println("composite.getClass().getName() = " + composite.getClass().getName());
    //    System.out.println("child.getClass().getName() = " +
    //    child.getClass().getName());
    System.out.println("composite.getClass().getModule() = " + composite.getClass().getModule());
    return new WeakReference<>(clazz);
  }

  public static void main(String... args) throws Exception {
    new MemoryLeakPrevention().testAgainstMemoryLeak();
  }

  public void testAgainstMemoryLeak() throws Exception {
    Generator gen = new Generator("mystery-meat",
        BaseComponent.class.getClassLoader());

    //    Class clazz =
    //        gen.make("A", "public interface A { void foo();
    //        }");
    Class clazz =
        gen.make("A", "public interface A extends eu" +
                          ".javaspecialists.books" +
                          ".dynamicproxies.handlers" +
                          ".BaseComponent<A> { void foo(); }").asSubclass(BaseComponent.class);

    Object proxy = Proxies.castProxy(clazz,
        (proxy1, method, args1) -> {
          System.out.println(method);
          return null;
        });
    clazz.getMethod("foo").invoke(proxy);
    System.out.println("proxy.getClass().getModule() = " + proxy.getClass().getModule());
    System.out.println("clazz.getModule() = " + clazz.getModule());
    System.out.println("proxy.getClass() = " + proxy.getClass());
    System.out.println("proxy.getClass().getClassLoader()" +
                           ".getName() = " + proxy.getClass().getClassLoader().getName());
    proxy = null;
    //    WeakReference<Class<?>> clazzRef =
    //        new WeakReference<>(clazz);
    gen = null;

    WeakReference<Class<?>> clazzRef =
        makeACompositeAndUseIt(clazz);
    clazz = null;

    for (int i = 0; clazzRef.get() != null && i < 10; i++) {
      //      CompositeHandler.clean();
      System.gc();
      Thread.sleep(200);
    }

    assertNull("Memory leak detected, most likely in " +
                   "CompositeHandler", clazzRef.get());
  }
}
