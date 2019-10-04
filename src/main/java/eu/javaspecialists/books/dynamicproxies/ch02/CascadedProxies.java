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

import java.time.LocalDateTime;

public class CascadedProxies {
    public interface Service {
        public String work(String txt);
    }

    static
    public class ServiceImpl implements Service {
        public ServiceImpl() {
            System.out.println(" ServiceImpl = " + LocalDateTime.now());
        }

        @Override
        public String work(String txt) {
            return "ServiceImpl - " + txt;
        }
    }
    static public class SecureProxy implements Service {

        private Service service;

        private SecureProxy(Builder builder) {
            System.out.println(" SecureProxy = " + LocalDateTime.now());
            setService(builder.service);
            setCode(builder.code);
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public static Builder newBuilder(SecureProxy copy) {
            Builder builder = new Builder();
            builder.service = copy.service;
            builder.code = copy.code;
            return builder;
        }

        public void setService(Service service) {
            this.service = service;
        }

        private String code = "";

        //Simmulation der Tastatureingabe
        public void setCode(String code) {
            this.code = code;
        }

        public String work(String txt) {
            if (code.equals("hoppel")) {
                return service.work(txt);
            } else {
                return "nooooop";
            }
        }


        public static final class Builder {
            private Service service;
            private String code;

            private Builder() {
            }

            public Builder withService(Service service) {
                this.service = service;
                return this;
            }

            public Builder withCode(String code) {
                this.code = code;
                return this;
            }

            public SecureProxy build() {
                return new SecureProxy(this);
            }
        }

    }

    static public class VirtualProxy implements Service {

        public ServiceFactory serviceFactory;
        public ServiceStrategyFactory serviceStrategyFactory;

        private VirtualProxy(Builder builder) {
            System.out.println(" VirtualProxy = " + LocalDateTime.now());
            serviceFactory = builder.serviceFactory;
            setServiceStrategyFactory(builder.serviceStrategyFactory);
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public static Builder newBuilder(VirtualProxy copy) {
            Builder builder = new Builder();
            builder.serviceFactory = copy.serviceFactory;
            builder.serviceStrategyFactory = copy.serviceStrategyFactory;
            return builder;
        }


        // tag::work()[]
        public String work(String txt) {
            return serviceStrategyFactory
                .realSubject(serviceFactory).work(txt);
        }
        // end::work()[]

        public VirtualProxy setServiceStrategyFactory(ServiceStrategyFactory serviceStrategyFactory) {
            this.serviceStrategyFactory = serviceStrategyFactory;
            return this;
        }


        /**
         * Strategy of creating: ThreadSafe, NotThreadSafe, ...
         */
        // tag::ServiceStrategyFactory[]
        public interface ServiceStrategyFactory {
            Service realSubject(ServiceFactory factory);
        }
        // end::ServiceStrategyFactory[]

        // tag::ServiceFactory[]
        public interface ServiceFactory {
            Service createInstance();
        }
        // end::ServiceFactory[]

        public static final class Builder {
            private ServiceFactory serviceFactory;
            private ServiceStrategyFactory serviceStrategyFactory;

            private Builder() {
            }

            public Builder withServiceFactory(ServiceFactory serviceFactory) {
                this.serviceFactory = serviceFactory;
                return this;
            }

            public Builder withServiceStrategyFactory(ServiceStrategyFactory serviceStrategyFactory) {
                this.serviceStrategyFactory = serviceStrategyFactory;
                return this;
            }

            public VirtualProxy build() {
                return new VirtualProxy(this);
            }
        }
    }

    static
    public class Main {
        public static void main(String... args) throws InterruptedException {

            // tag::main[]
            var strategyFactory =
                new VirtualProxy.ServiceStrategyFactory() {
                    Service service = null; //no lambda
                    @Override
                    public Service realSubject(
                        VirtualProxy.ServiceFactory factory) {
                        if (service == null) {
                            service = factory.createInstance();
                        }
                        return service;
                    }
                };
            var virtualProxyBuilder = VirtualProxy.newBuilder()
                .withServiceFactory(ServiceImpl::new)
                .withServiceStrategyFactory(strategyFactory);

            var secureProxyBuilder = SecureProxy.newBuilder()
                .withCode("ccc")
                .withService(virtualProxyBuilder.build());

            var proxy = secureProxyBuilder.build();

            Thread.sleep(5000);
            System.out.println(proxy.work("AA"));
            proxy.setCode("hoppel");
            System.out.println(proxy.work("AA"));

            // Output:
            //  VirtualProxy = 2019-09-24T15:22:53.402476
            // SecureProxy = 2019-09-24T15:22:53.407328
            //nooooop
            // ServiceImpl = 2019-09-24T15:22:58.412393
            //ServiceImpl - AA
            // end::main[]

            // tag::secureProxy[]
            Service secureProxy = SecureProxy.newBuilder()
                .withCode("hoppel")
                .withService(virtualProxyBuilder.build())
                .build();
            // end::secureProxy[]

            System.out.println(secureProxy.work("AA").equals("ServiceImpl - " +
                "AA"));

        }
    }
}
