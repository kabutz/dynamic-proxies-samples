/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
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

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.*;
import java.util.concurrent.*;

// tag::listing[]
public class CanadianEmbassy implements Canada {
  private final HttpClient httpClient =
      HttpClient.newBuilder()
          .followRedirects(HttpClient.Redirect.NORMAL)
          .build();
  @Override
  public boolean canGetVisa(String name,
                            boolean married,
                            boolean rich) {
    try {
      var encoded_name = URLEncoder.encode(name,
          StandardCharsets.UTF_8);
      var url = "http://0.0.0.0:8080/canGetVisa/" +
                    encoded_name + "/" +
                    married + "/" + rich;
      var req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
      var res = httpClient.send(
          req, HttpResponse.BodyHandlers.ofString());
      return "true".equals(res.body());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new CancellationException("interrupted");
    }
  }
}
// end::listing[]