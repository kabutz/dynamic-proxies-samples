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

package eu.javaspecialists.books.dynamicproxies.ch03.scratch;

import eu.javaspecialists.books.dynamicproxies.*;
import org.junit.*;

import java.lang.reflect.*;

import static org.junit.Assert.*;

public class InvalidProxyCalls {
    private final static InvocationHandler UNSUPPORTED =
            (proxy, method, args) -> {
                throw new UnsupportedOperationException("not implemented");
            };

    @Test(expected = IllegalArgumentException.class)
    public void classIsString() {
        castProxy(String.class, UNSUPPORTED);
    }
    @Test(expected = IllegalArgumentException.class)
    public void classIsPrimitive() {
        castProxy(int.class, UNSUPPORTED);
    }
    @Test(expected = IllegalArgumentException.class)
    public void classIsAbstractNumber() {
        castProxy(Number.class, UNSUPPORTED);
    }
    @Test(expected = IllegalArgumentException.class)
    public void duplicateClasses() {
        Proxy.newProxyInstance(
                Runnable.class.getClassLoader(),
                new Class<?>[] {Runnable.class, Runnable.class},
                UNSUPPORTED
        );
    }

    @Test
    public void packageAccessInterface() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(
                getClass().getPackageName() + ".hidden.Foo");
        Object proxy = Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[] {clazz},
                UNSUPPORTED
        );
        assertEquals(clazz.getPackageName(),
                proxy.getClass().getPackageName());
    }

    @Test
    public void twoPackageAccessInterfaces() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(
                getClass().getPackageName() + ".hidden.Foo");
        Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[] {Foo.class, clazz},
                UNSUPPORTED
        );
    }

    @Test
    public void packageName() throws ClassNotFoundException {
        Object proxy = Proxy.newProxyInstance(
                Runnable.class.getClassLoader(),
                new Class<?>[] {Runnable.class},
                UNSUPPORTED
        );
        System.out.println(proxy.getClass());
        System.out.println(proxy.getClass().getPackageName());
        System.out.println(proxy.getClass().getClassLoader());
    }

    @Test
    public void testDifferentClassLoaders() throws ClassNotFoundException {
        Object proxy = Proxy.newProxyInstance(
                Runnable.class.getClassLoader(),
                new Class<?>[] {Foo.class},
                UNSUPPORTED
        );
        System.out.println(proxy.getClass());
        System.out.println(proxy.getClass().getPackageName());
    }
    @Test
    public void testDifferentClassLoaders2() throws ClassNotFoundException {
        Object proxy = Proxy.newProxyInstance(
                Foo.class.getClassLoader(),
                new Class<?>[] {Runnable.class},
                UNSUPPORTED
        );
        System.out.println(proxy.getClass());
        System.out.println(proxy.getClass().getPackageName());
    }

    @Test
    public void testDifferentClassLoaders3() throws ClassNotFoundException {
        System.out.println(ClassLoader.getPlatformClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());
        Object proxy = Proxy.newProxyInstance(
                Foo.class.getClassLoader(),
                new Class<?>[] {Foo.class},
                UNSUPPORTED
        );
        System.out.println(proxy.getClass());
        System.out.println(proxy.getClass().getPackageName());
        System.out.println(proxy.getClass().getClassLoader());
        System.out.println(Runnable.class.getClassLoader());
    }

    private static <P> P castProxy(Class<P> proxiedInterface,
                                   InvocationHandler handler) {
        return proxiedInterface.cast(Proxy.newProxyInstance(
                proxiedInterface.getClassLoader(),
                new Class<?>[] {proxiedInterface}, handler
        ));
    }

    public static void main(String... args) {

        /* <p>
         * <li>All of the interface types must be visible by name
         * through the
         * specified class loader. In other words, for class loader
         * {@code cl} and every interface {@code i}, the following
         * expression must be true:<p>
         * {@code Class.forName(i.getName(), false, cl) == i}
         *
         * <li>All of the types referenced by all
         * public method signatures of the specified interfaces
         * and those inherited by their superinterfaces
         * must be visible by name through the specified class loader.
         *
         * <li>All non-public interfaces must be in the same package
         * and module, defined by the specified class loader and
         * the module of the non-public interfaces can access all of
         * the interface types; otherwise, it would not be possible for
         * the proxy class to implement all of the interfaces,
         * regardless of what package it is defined in.
         *
         * <li>For any set of member methods of the specified interfaces
         * that have the same signature:
         * <ul>
         * <li>If the return type of any of the methods is a primitive
         * type or void, then all of the methods must have that same
         * return type.
         * <li>Otherwise, one of the methods must have a return type that
         * is assignable to all of the return types of the rest of the
         * methods.
         * </ul>
         *
         * <li>The resulting proxy class must not exceed any limits imposed
         * on classes by the virtual machine.  For example, the VM may
         * limit
         * the number of interfaces that a class may implement to 65535; in
         * that case, the size of the {@code interfaces} array must not
         * exceed 65535.
         * </ul>
         *
         * <p>Note that the order of the specified proxy interfaces is
         * significant: two requests for a proxy class with the same
         * combination
         * of interfaces but in a different order will result in two
         * distinct
         * proxy classes.
         */
    }
}
