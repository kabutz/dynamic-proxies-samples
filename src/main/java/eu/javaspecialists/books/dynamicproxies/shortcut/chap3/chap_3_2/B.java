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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap3.chap_3_2;

/**
 * Created by sven on 20.01.15.
 */
// real subject
public final class B implements A {
    private final int i;

    public B(int i) {
        this.i = i;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof A)) return false;

        if (getClass() == o.getClass()) {
            B b = (B) o;
            return i == b.i;
        }

        return o.equals(this);
    }

    public int hashCode() {
        return i;
    }
}
