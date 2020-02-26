/*
 * Copyright (C) 2020 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz
 * licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */

package eu.javaspecialists.books.dynamicproxies.ch02.remote;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

// tag::listing[]
public class ServicePublisher {
  public static void main(String... args) throws Exception {
    Canada canada = new RealCanada();

    var server = new Server(8080);
    server.setHandler(new AbstractHandler() {
      @Override
      public void handle(String target, Request jettyRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        jettyRequest.setHandled(true);
        var uri = request.getRequestURI();
        var pattern = Pattern.compile("/canGetVisa/(?<name>[^/]+)/(?<married>[^/]+)/(?<rich>[^/]+)");
        var matcher = pattern.matcher(uri);
        if (matcher.matches()) {
          var name = matcher.group("name");
          var married = Boolean.valueOf(matcher.group("married"));
          var rich = Boolean.valueOf(matcher.group("rich"));
          var canGetVisa = canada.canGetVisa(name, married, rich);
          response.getOutputStream().print(canGetVisa);
        } else {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
      }
    });
    server.start();
  }
}
// end::listing[]

