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

package eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_2.remote;


import eu.javaspecialists.books.dynamicproxies.shortcut.chap2.c_2_2.Service;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 1st argument service URI, refer to wsdl document above
 * 2nd argument is service name, refer to wsdl document above
 * Created by Sven Ruppert on 22.09.2014.
 */
public class ServiceRemoteProxy implements Service {

    private URL url;
    private Service realSubject;

    public ServiceRemoteProxy() {
        try {
            url = new URL("http://localhost:9999/ws/service?wsdl");
            final String namespaceURI = "http://c_2_2.chap2.proxies" +
                ".shortcut" +
                ".entwicklerpress.demo.rapidpm.org/";
            final String localPart = "ServiceImplService";
            QName qname = new QName(namespaceURI, localPart);
            javax.xml.ws.Service service = javax.xml.ws.Service.create(url,
                qname);
            realSubject = service.getPort(Service.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String work(String txt) {
        return realSubject.work(txt);
    }
}
