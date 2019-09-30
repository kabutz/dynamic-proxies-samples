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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap1.c_1_1;


import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.model.Service;
import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.model.ServiceImpl;

/**
 * Kein Unterschied zum Delegator!!
 * <p>
 * Created by Sven Ruppert on 22.09.2014.
 */
public class ServiceProxy implements Service {
    private Service service = new ServiceImpl();

    public String work(String txt) {
        return service.work(txt);
    }
}
