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

package eu.javaspecialists.books.dynamicproxies.util;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// tag::listing[]
public class VTable {
  public static final int NOT_FOUND = -1;
  private final Method[] entries;
  private final Class<?>[][] paramTypes;
  private final boolean[] distinctName;
  private MethodHandle[] defaultMethods;
  private final int size;

  private final int mask;

  private VTable(Collection<Method> methods,
                 Set<String> distinctMethodNames,
                 boolean includeDefaultMethods) {
    this.size = methods.size();
    mask = Math.max((-1 >>> Integer.numberOfLeadingZeros(
        size * 4 - 1)), 127);
    entries = new Method[mask + 1];
    paramTypes = new Class<?>[entries.length][];
    distinctName = new boolean[entries.length];
    defaultMethods = new MethodHandle[entries.length];
    for (var method : methods) {
      put(method,
          distinctMethodNames.contains(method.getName()),
          includeDefaultMethods);
    }
    methods.forEach(MethodTurboBooster::boost);
  }

  private void put(Method method, boolean distinct,
                   boolean includeDefaultMethods) {
    int offset = offset(method);
    var methodParamTypes = ParameterTypesFetcher.get(method);
    while (entries[offset] != null) {
      if (entries[offset].getName() == method.getName()
              && matches(paramTypes[offset], methodParamTypes))
        throw new IllegalArgumentException(
            "Duplicate method found: " + new MethodKey(method));
      offset = (offset + 1) & mask;
    }
    entries[offset] = method;
    paramTypes[offset] = methodParamTypes;
    distinctName[offset] = distinct;
    if (includeDefaultMethods && method.isDefault()) {
      defaultMethods[offset] = createDefaultMethodHandle(method);
    }
  }

  private MethodHandle createDefaultMethodHandle(Method method) {
    try {
      // Thanks Thomas Darimont for this idea
      Class<?> target = method.getDeclaringClass();
      MethodHandles.Lookup lookup = MethodHandles.lookup();
      if (target.getModule().isOpen(
          method.getDeclaringClass().getPackageName(),
          VTable.class.getModule())) {
        return MethodHandles.privateLookupIn(target, lookup)
                   .in(target)
                   .unreflectSpecial(method, target);
      } else {
        return null;
      }
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private int offset(Method method) {
    return (method.getName().hashCode() +
                method.getParameterCount()) & mask;
  }

  private int findIndex(Method method) {
    int offset = offset(method);
    Class<?>[] methodParamTypes = null;
    Method match;
    while ((match = entries[offset]) != null) {
      if (match.getName() == method.getName()) {
        if (distinctName[offset]) return offset;
        if (methodParamTypes == null)
          methodParamTypes = ParameterTypesFetcher.get(method);
        if (matches(paramTypes[offset], methodParamTypes))
          return offset;
      }
      offset++;
    }
    return NOT_FOUND;
  }

  public Method lookup(Method method) {
    int index = findIndex(method);
    return index == NOT_FOUND ? null : entries[index];
  }

  public MethodHandle lookupDefaultMethod(Method method) {
    int index = findIndex(method);
    return index == NOT_FOUND ? null : defaultMethods[index];
  }

  private boolean matches(Class<?>[] types1, Class<?>[] types2) {
    if (types1.length != types2.length) return false;
    for (int i = 0; i < types1.length; i++) {
      if (types1[i] != types2[i]) return false;
    }
    return true;
  }

  public int size() {
    return size;
  }

  public Stream<Method> stream() {
    return Stream.of(entries)
               .filter(Objects::nonNull);
  }

  public Stream<Method> streamDefaultMethods() {
    return IntStream.iterate(0, i -> i < entries.length,
        i -> i + 1)
               .filter(i -> defaultMethods[i] != null)
               .mapToObj(i -> entries[i]);
  }

  /**
   * Returns true if method is overloaded and false if it is
   * distinct.
   *
   * @throws IllegalArgumentException if method is not in VTable
   */
  public boolean isOverloaded(Method method) {
    int offset = offset(method);
    Method match;
    while ((match = entries[offset]) != null) {
      if (match.getName() == method.getName()) {
        return !distinctName[offset];
      }
      offset++;
    }
    throw new IllegalArgumentException("Method not found");
  }

  public static class Builder {
    private static final Method[] objectMethods;
    public static final BinaryOperator<Method> MOST_SPECIFIC_RETURN =
        (method1, method2) -> {
          var r1 = method1.getReturnType();
          var r2 = method2.getReturnType();
          if (r2.isAssignableFrom(r1)) {
            return method1;
          }
          if (r1.isAssignableFrom(r2)) {
            return method2;
          } else {
            throw new IllegalStateException(
                method1 + " and " + method2 +
                    " have incompatible return types");
          }
        };
    static {
      try {
        objectMethods = new Method[] {
            Object.class.getMethod("toString"),
            Object.class.getMethod("hashCode"),
            Object.class.getMethod("equals", Object.class),
        };
      } catch (NoSuchMethodException e) {
        throw new Error(e);
      }
    }
    private final Map<MethodKey, Method> receiverClassMap;
    private Class<?> defaultMethods;
    private Collection<Class<?>> targetInterfaces =
        new ArrayList<>();
    private boolean inludeObjectMethods = true;
    private boolean includeDefaultMethods = false;

    /**
     * @param receiver The class that receives the actual method
     *                 calls.  Does not have to be related to the
     *                 dynamic proxy interfaces.
     */
    public Builder(Class<?> receiver) {
      receiverClassMap = createPublicMethodMap(receiver);
    }

    public Builder excludeObjectMethods() {
      this.inludeObjectMethods = false;
      return this;
    }

    public Builder addTargetInterface(Class<?> targetInterface) {
      if (!targetInterface.isInterface())
        throw new IllegalArgumentException(
            targetInterface.getCanonicalName() +
                " is not an interface");
      this.targetInterfaces.add(targetInterface);
      return this;
    }

    public Builder includeDefaultMethods() {
      this.includeDefaultMethods = true;
      return this;
    }

    public VTable build() {
      // Build collection of all methods from the target
      // interfaces, as well as the three Object methods if
      // included: toString(), equals(Object), hashCode()
      Collection<Method> allMethods =
          targetInterfaces.stream()
              .flatMap(clazz -> Stream.of(clazz.getMethods()))
              .collect(Collectors.toList());
      if (inludeObjectMethods) {
        for (Method method : objectMethods) {
          allMethods.add(method);
        }
      }

      // Reduce the methods to the most derived return type when
      // two have the same name and parameter types.  If we find
      // two methods with incompatible return types, e.g. Integer
      // and String, then throw an exception.
      Map<MethodKey, Method> targetMethods =
          allMethods.stream()
              .collect(Collectors.toUnmodifiableMap(
                  MethodKey::new,
                  Function.identity(),
                  MOST_SPECIFIC_RETURN));

      // Find all those methods that have a unique name, thus no
      // overloading.  This will speed up matching.
      Set<String> distinctMethodNames =
          targetMethods.values().stream()
              .collect(Collectors.groupingBy(Method::getName,
                  Collectors.counting()))
              .entrySet()
              .stream()
              .filter(entry -> entry.getValue() == 1L)
              .map(Map.Entry::getKey)
              .collect(Collectors.toSet());

      Collection<Method> matchedMethods =
          targetMethods.entrySet().stream()
              .map(this::filterOnReturnType)
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

      return new VTable(matchedMethods, distinctMethodNames,
          includeDefaultMethods);
    }

    /**
     * Ensure that receiverClassMap method return type of method
     * can be cast to the target method return type.
     */
    private Method filterOnReturnType(
        Map.Entry<MethodKey, Method> entry) {
      var targetMethod = entry.getValue();
      var receiverMethod = receiverClassMap.get(entry.getKey());
      if (receiverMethod != null) {
        var targetReturn = targetMethod.getReturnType();
        var receiverReturn = receiverMethod.getReturnType();
        if (targetReturn.isAssignableFrom(receiverReturn))
          return receiverMethod;
      }
      return null;
    }

    /**
     * Our reverse map allows us to find the methods in the
     * component that we are decorating.
     */
    private Map<MethodKey, Method> createPublicMethodMap(
        Class<?> clazz) {
      Map<MethodKey, Method> map = new HashMap<>();
      addTrulyPublicMethods(clazz, map);
      return map;
    }

    /**
     * Recursively add all "truly" public methods from this
     * class,
     * superclasses and interfaces.  By "truly" we mean methods
     * that are public and which are defined inside public
     * classes.
     */
    private void addTrulyPublicMethods(
        Class<?> clazz, Map<MethodKey, Method> map) {
      if (clazz == null) return;
      for (var method : clazz.getMethods()) {
        if (isTrulyPublic(method)) {
          MethodKey key = new MethodKey(method);
          map.merge(key, method, MOST_SPECIFIC_RETURN);
        }
      }
      for (var anInterface : clazz.getInterfaces()) {
        addTrulyPublicMethods(anInterface, map);
      }
      addTrulyPublicMethods(clazz.getSuperclass(), map);
    }

    /**
     * Truly public are those methods where the declaring class
     * is also public, hence the bitwise AND.
     */
    private boolean isTrulyPublic(Method method) {
      return Modifier.isPublic(
          method.getModifiers()
              & method.getDeclaringClass().getModifiers());
    }
  }
}
// end::listing[]