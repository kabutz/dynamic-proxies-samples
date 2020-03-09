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

package eu.javaspecialists.books.dynamicproxies.ch03.virtual.moralfibre;

public class Company {
  private final String name;
  private int cash;
  private final MoralFibre moralFibre;

  public Company(String name, int cash, MoralFibre moralFibre) {
    this.name = name;
    this.cash = cash;
    this.moralFibre = moralFibre;
  }
  public void makeMoney() {
    System.out.println("Oh goodie!");
    cash *= 2;
  }
  public void damageEnvironment() {
    System.out.println("Oops, sorry about that oilspill...");
    cash *= 4;
  }
  // Oopsie
  public void becomeFocusOfMediaAttention() {
    System.out.println("Look how good we are...");
    cash -= moralFibre.actSociallyResponsibly();
    cash -= moralFibre.cleanupEnvironment();
    cash -= moralFibre.empowerEmployees();
  }

  @Override
  public String toString() {
    return String.format("%s has $ %d", name, cash);
  }
}