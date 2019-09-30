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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap09_1;

/**
 * Created by Sven Ruppert on 18.12.2014.
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Proxies {
    public static <T> T makeSynchronized(Class<T> subjectIntf,
                                         T realSubject) {
        SynchronizedInvocationHandler handler =
            new SynchronizedInvocationHandler(realSubject);
        T result = makeProxy(subjectIntf, handler);
        handler.setLock(result);
        return result;
    }

    public static <T> T makeSynchronized(Class<T> subjectIntf,
                                         T realSubject, Object lock) {
        SynchronizedInvocationHandler handler =
            new SynchronizedInvocationHandler(realSubject);
        T result = makeProxy(subjectIntf, handler);
        handler.setLock(lock);
        return result;
    }

    private static <T> T makeProxy(Class<T> subjectIntf,
                                   SynchronizedInvocationHandler handler) {
        return (T) Proxy.newProxyInstance(
            subjectIntf.getClassLoader(),
            new Class[]{subjectIntf},
            handler);
    }

    private static class SynchronizedInvocationHandler implements InvocationHandler {
        private Object lock;
        private final Object realSubject;

        public SynchronizedInvocationHandler(Object realSubject) {
            this.realSubject = realSubject;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            synchronized (lock) {
                Object result = method.invoke(realSubject, args);
                if (method.getReturnType().isInterface()) {
                    return makeSynchronized((Class) method.getReturnType(), result
                        , lock);
                }
                return result;
            }
        }

        public void setLock(Object lock) {
            this.lock = lock;
        }
    }
}
