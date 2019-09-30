/*
 * Copyright (c) 2014. Heinz Max Kabutz , Sven Ruppert
 *   We licenses
 *   this file to you under the Apache License, Version 2.0 (the
 * "License");
 *   you may not use this file except in compliance with the License.
 * You may
 *   obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.shortcut.chap_4_0;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Sven Ruppert on 04.01.14.
 */
public class GenericFactory {
    public static Collection makeCollection(Collection backing,
                                            Class type) {
        GenericCollection gen = new GenericCollection(backing, type);
        return (Collection) Proxy.newProxyInstance(
            gen.getTypeCollectionClass().getClassLoader(),
            new Class[]{gen.getTypeCollectionClass()},
            gen);
    }

    /* please ignore makeIterator for now ... */
    public static Iterator makeIterator(Iterator backing, Class type) {
        GenericIterator gen = new GenericIterator(backing, type);
        return (Iterator) Proxy.newProxyInstance(
            gen.getTypeIteratorClass().getClassLoader(),
            new Class[]{gen.getTypeIteratorClass()},
            gen);
    }
}
