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

package eu.javaspecialists.books.dynamicproxies.handlers;

/**
 * Used by #CompositeHandler
 */

// tag::listing[]
import java.util.function.*;

public class Reducer {
  private final Object identity;
  private final BinaryOperator<Object> merger;
  public Reducer(Object identity,
                 BinaryOperator<Object> merger) {
    this.merger = merger;
    this.identity = identity;
  }
  public Object getIdentity() {
    return identity;
  }
  public BinaryOperator<Object> getMerger() {
    return merger;
  }
  public static final Reducer NULL_REDUCER =
      new Reducer(null, (o1, o2) -> null);
  /**
   * Result will be substituted with the proxy instance.
   */
  public static final Reducer PROXY_INSTANCE_REDUCER =
      new Reducer(null, (o1, o2) -> null);
}
// end::listing[]