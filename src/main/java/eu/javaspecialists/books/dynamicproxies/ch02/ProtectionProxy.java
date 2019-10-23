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

package eu.javaspecialists.books.dynamicproxies.ch02;

import eu.javaspecialists.books.dynamicproxies.ch02.virtual.*;

import java.util.concurrent.locks.*;
import java.util.stream.*;

public class ProtectionProxy {
    static
    // tag::SynchronizedMap[]
    public class CustomSynchronizedMap<K, V> implements CustomMap<K, V> {
        private final CustomMap<K, V> realMap;
        public CustomSynchronizedMap(CustomMap<K, V> realMap) {
            this.realMap = realMap;
        }
        @Override
        public synchronized int size() {
            return realMap.size();
        }
        @Override
        public synchronized V get(Object key) {
            return realMap.get(key);
        }
        @Override
        public synchronized V put(K key, V value) {
            return realMap.put(key, value);
        }
        @Override
        public synchronized V remove(Object key) {
            return realMap.remove(key);
        }
        @Override
        public synchronized void clear() {
            realMap.clear();
        }
    }
    // end::SynchronizedMap[]

    static
    // tag::ReentrantLockMap[]
    public class CustomReentrantLockMap<K, V> implements CustomMap<K, V> {
        private final Lock lock = new ReentrantLock();
        private final CustomMap<K, V> realMap;
        public CustomReentrantLockMap(CustomMap<K, V> realMap) {
            this.realMap = realMap;
        }
        @Override
        public int size() {
            lock.lock();
            try {
                return realMap.size();
            } finally {
                lock.unlock();
            }
        }
        @Override
        public V get(Object key) {
            lock.lock();
            try {
                return realMap.get(key);
            } finally {
                lock.unlock();
            }
        }
        @Override
        public V put(K key, V value) {
            lock.lock();
            try {
                return realMap.put(key, value);
            } finally {
                lock.unlock();
            }
        }
        @Override
        public V remove(Object key) {
            lock.lock();
            try {
                return realMap.remove(key);
            } finally {
                lock.unlock();
            }
        }
        @Override
        public void clear() {
            lock.lock();
            try {
                realMap.clear();
            } finally {
                lock.unlock();
            }
        }
    }
    // end::ReentrantLockMap[]

    static
    // tag::UnmodifiableMap[]
    public class CustomUnmodifiableMap<K, V> implements CustomMap<K, V> {
        private final CustomMap<K, V> realMap;
        public CustomUnmodifiableMap(CustomMap<K, V> realMap) {
            this.realMap = realMap;
        }
        @Override
        public int size() {
            return realMap.size();
        }
        @Override
        public V get(Object key) {
            return realMap.get(key);
        }
        @Override
        public V put(K key, V value) {
            throw new UnsupportedOperationException("unmodifiable");
        }
        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException("unmodifiable");
        }
        @Override
        public void clear() {
            throw new UnsupportedOperationException("unmodifiable");
        }
    }
    // end::UnmodifiableMap[]

    static
    // tag::Main[]
    public class Main {
        public static void main(String... args) {
            test(new CustomHashMap<>());
            test(new CustomSynchronizedMap<>(new CustomHashMap<>()));
            test(new CustomReentrantLockMap<>(new CustomHashMap<>()));
            test(new CustomUnmodifiableMap<>(new CustomHashMap<>()));
        }
        private static void test(CustomMap<Integer, Integer> squares) {
            IntStream.range(0, 46000)
                    .parallel()
                    .forEach(i -> squares.put(i, i * i));
            int size = squares.size();
            if (size == 46000)
                System.out.println("Perfect size of " + size);
            else
                System.err.println("size should be 46000, but is " + size);
        }
    }
    // end::Main[]
}
