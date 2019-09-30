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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap_5_0;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Sven Ruppert on 05.01.14.
 */
public class BetterArrayList<T> extends ArrayList<T> {

    private final Class<T> valueType;

    public BetterArrayList(int initialCapacity, Class<T> valueType) {
        super(initialCapacity);
        this.valueType = valueType;
    }

    public BetterArrayList(Class<T> valueType) {
        this.valueType = valueType;
    }

    public BetterArrayList(Collection<? extends T> ts,
                           Class<T> valueType) {
        super(ts);
        this.valueType = valueType;
    }

    // You can modify the return type of an overridden method in
    // Java 5, with some restrictions.
    public T[] toArray() {
        return toArray((T[]) Array.newInstance(valueType, size()));
    }

    public static void main(String[] args) {
        BetterArrayList<String> names =
            new BetterArrayList<String>(String.class);
        names.add("Wolfgang");
        names.add("Leander");
        names.add("Klaus");
        names.add("Reinhard");
        String[] nameArray = names.toArray();
        for (String s : nameArray) {
            System.out.println(s);
        }
    }
}
