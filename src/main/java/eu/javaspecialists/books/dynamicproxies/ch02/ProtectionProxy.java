package eu.javaspecialists.books.dynamicproxies.ch02;

import eu.javaspecialists.books.dynamicproxies.ch02.VirtualProxy.*;

import java.util.concurrent.locks.*;
import java.util.stream.*;

public class ProtectionProxy {
    static
    // tag::SynchronizedMap[]
    public class SynchronizedMap<K, V> implements Map<K, V> {
        private final Map<K, V> realMap;
        public SynchronizedMap(Map<K, V> realMap) {
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
    public class ReentrantLockMap<K, V> implements Map<K, V> {
        private final Lock lock = new ReentrantLock();
        private final Map<K, V> realMap;
        public ReentrantLockMap(Map<K, V> realMap) {
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
    public class UnmodifiableMap<K, V> implements Map<K, V> {
        private final Map<K, V> realMap;
        public UnmodifiableMap(Map<K, V> realMap) {
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
            test(new HashMap<>());
            test(new SynchronizedMap<>(new HashMap<>()));
            test(new ReentrantLockMap<>(new HashMap<>()));
            test(new UnmodifiableMap<>(new HashMap<>()));
        }
        private static void test(Map<Integer, Integer> squares) {
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
