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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.proxy;


import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_4.model.Service;

import java.time.LocalDateTime;

/**
 * Created by Sven Ruppert on 17.12.2014.
 */
public class SecureProxy implements Service {

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
