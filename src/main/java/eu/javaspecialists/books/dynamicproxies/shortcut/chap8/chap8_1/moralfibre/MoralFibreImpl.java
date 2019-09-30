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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap8.chap8_1.moralfibre;

/**
 * Created by Sven Ruppert on 14.01.14.
 */
public class MoralFibreImpl implements MoralFibre {
    // very expensive to create moral fibre!
    private byte[] costOfMoralFibre = new byte[900 * 1000];

    {
        System.out.println("Moral Fibre Created!");
    }

    // AIDS orphans
    public double actSociallyResponsibly() {
        return costOfMoralFibre.length / 3;
    }

    // shares to employees
    public double empowerEmployees() {
        return costOfMoralFibre.length / 3;
    }

    // oiled sea birds
    public double cleanupEnvironment() {
        return costOfMoralFibre.length / 3;
    }
}
