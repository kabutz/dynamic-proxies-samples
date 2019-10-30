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

package eu.javaspecialists.books.dynamicproxies.ch07;

public class ProxyGenerator {
    /*
    private static final Map<ClassLoader, Map<CacheKey, Class<?>>> cache =
            new WeakHashMap<>();
    public static <T> T make(Class<T> subject,
                             Supplier<? extends T> supplier,
                             Concurrency concurrency, ProxyType type) {
        return make(subject.getClassLoader(), subject, supplier,
                concurrency, type);
    }
    public static <T> T make(
            Class<T> subject, Supplier<? extends T> supplier,
            Concurrency concurrency) {
        return make(subject, supplier, concurrency, ProxyType.STATIC);
    }
    public static <T> T make(ClassLoader loader, Class<T> subject,
                             Supplier<? extends T> supplier,
                             Concurrency concurrency, ProxyType type) {
        Object proxy = null;
        if (type == ProxyType.STATIC) {
            proxy = createStaticProxy(subject, supplier, concurrency);
        } else if (type == ProxyType.DYNAMIC) {
            proxy = createDynamicProxy(loader, subject, supplier,
                    concurrency);
        }
        return subject.cast(proxy);
    }
    private static <T> T createStaticProxy(
            Class<T> subject, Supplier<? extends T> supplier,
            Concurrency concurrency) {
        //        Map<CacheKey, Class<?>> clcache;
        //        synchronized (cache) {
        //            clcache = cache.get(loader);
        //            if (clcache == null) {
        //                cache.put(loader, clcache = new HashMap<>());
        //            }
        //        }
        try {
            Class<?> clazz;
            //            CacheKey key = new CacheKey(subject,
            //            concurrency);
            //            synchronized (clcache) {
            //                clazz = clcache.get(key);
            //                if (clazz == null) {
            VirtualProxySourceGenerator vpsg = create(subject,
                    supplier, concurrency);
            clazz = Generator.make(vpsg.getProxyName(),
                    vpsg.getCharSequence());
            //                    clcache.put(key, clazz);
            //                }

            //            }
            return subject.cast(clazz.newInstance());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }
    private static <T> VirtualProxySourceGenerator create(
            Class<?> subject, Supplier<? extends T> supplier,
            Concurrency concurrency) {
        return switch (concurrency) {
            case NONE -> new VirtualProxySourceGeneratorBasic(
                    subject, supplier);
            case LOCK_FREE -> new VirtualProxySourceGeneratorLockFree(
                    subject, supplier);
            case SYNCHRONIZED -> new
            VirtualProxySourceGeneratorSynchronized(
                    subject, supplier);
            default -> throw new IllegalArgumentException(
                    "Unsupported Concurrency: " + concurrency);
        };
    }
    private static <P> P createDynamicProxy(
            ClassLoader loader, Class<P> subject,
            Supplier<? extends P> realSupplier,
            Concurrency concurrency) {
        InvocationHandler handler = getDynamicProxyHandler(
                concurrency, realSupplier);
        return subject.cast(Proxy.newProxyInstance(
                loader,
                new Class<?>[] {subject},
                handler));
    }
    private static <P> InvocationHandler getDynamicProxyHandler(
            Concurrency concurrency, Supplier<? extends P> supplier) {
        return switch (concurrency) {
            default -> throw new IllegalArgumentException(
                    "Unsupported Concurrency: " + concurrency);
            case NONE -> new VirtualDynamicProxyBasic<>(supplier);
            case SYNCHRONIZED -> new VirtualDynamicProxySynchronized<>
            (supplier);
            case LOCK_FREE -> new VirtualDynamicProxyLockFree<>(supplier);
        };
    }
    private static class CacheKey {
        private final Class<?> subject;
        private final Concurrency concurrency;
        private CacheKey(Class<?> subject, Concurrency concurrency) {
            this.subject = subject;
            this.concurrency = concurrency;
        }
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey that = (CacheKey) o;
            if (concurrency != that.concurrency) return false;
            return subject.equals(that.subject);
        }
        public int hashCode() {
            return 31 * subject.hashCode() + concurrency.hashCode();
        }
    }
     */
}