/*
 * Copyright (c) 2014. Heinz Max Kabutz , Sven Ruppert
 *   We licenses
 *   this file to you under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License. You may
 *   obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.proxy;


import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.model.*;

import java.time.*;

/**
 * Created by Sven Ruppert on 17.12.2014.
 */
public class VirtualProxy implements Service {

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


  public String work(String txt) {
    return serviceStrategyFactory.realSubject(serviceFactory).work(txt);
  }

  public VirtualProxy setServiceStrategyFactory(ServiceStrategyFactory serviceStrategyFactory) {
    this.serviceStrategyFactory = serviceStrategyFactory;
    return this;
  }


  /**
   * Strategy of creating: ThreadSave, NotThreadSave, ...
   */
  public static interface ServiceStrategyFactory {
    Service realSubject(ServiceFactory factory);
  }

  public static interface ServiceFactory {
    Service createInstance();
  }


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
