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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4;


import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.model.Service;
import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.model.ServiceImpl;
import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.proxy.SecureProxy;
import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.proxy.VirtualProxy;

/**
 * Created by Sven Ruppert on 17.12.2014.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        final VirtualProxy.ServiceStrategyFactory strategyFactory =
            new VirtualProxy.ServiceStrategyFactory() {
                Service service = null; //no lambda

                @Override
                public Service realSubject(VirtualProxy.ServiceFactory factory) {
                    if (service == null) {
                        service = factory.createInstance();
                    }
                    return service;
                }
            };

        final VirtualProxy.Builder virtualProxyBuilder =
            VirtualProxy.newBuilder()
                .withServiceFactory(ServiceImpl::new)
                //        .withServiceFactory(aBuilder::build)
                .withServiceStrategyFactory(strategyFactory);

        final SecureProxy.Builder aBuilder = SecureProxy.newBuilder()
            .withCode("ccc")
            //        .withService(secureProxy);
            .withService(virtualProxyBuilder.build());

        final SecureProxy proxy = aBuilder.build();

        Thread.sleep(5000);
        System.out.println(proxy.work("AA").equals("ServiceImpl - AA"));
        proxy.setCode("hoppel");
        System.out.println(proxy.work("AA").equals("ServiceImpl - AA"));

        final Service secureProxy = SecureProxy.newBuilder()
            .withCode("hoppel")
            .withService(virtualProxyBuilder.build())
            .build();

        System.out.println(secureProxy.work("AA").equals("ServiceImpl - AA"));
    }
}
