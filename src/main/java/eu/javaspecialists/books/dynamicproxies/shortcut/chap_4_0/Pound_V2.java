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

import java.util.Arrays;

/**
 * Created by Sven Ruppert on 04.01.14.
 */
public class Pound_V2 {
    private Dog.Collection dogs;
    private Cat.Collection cats;

    public Pound_V2(Dog[] dogs, Cat[] cats) {
        this.dogs = (Dog.Collection) GenericFactory.makeCollection(
            Arrays.asList(dogs), Dog.class);
        this.cats = (Cat.Collection) GenericFactory.makeCollection(
            Arrays.asList(cats), Cat.class);
    }

    public void makeNoise() {
        Dog.Iterator dog_it = dogs.dogIterator();
        while (dog_it.hasNext()) {
            /* no more downcasting! */
            dog_it.nextDog().bark();
        }
        Cat.Iterator cat_it = cats.catIterator();
        while (cat_it.hasNext()) {
            /* no more downcasting! */
            cat_it.nextCat().meow();
        }
    }
}
