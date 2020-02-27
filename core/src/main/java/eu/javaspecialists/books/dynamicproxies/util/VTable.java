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

package eu.javaspecialists.books.dynamicproxies.util;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// tag::listing[]
/**
 * The purpose of the VTable is as a fast mapper between
 * interface methods and receiver class methods. To minimize
 * object creation, we avoid a normal HashMap, choosing rather
 * to store all the elements in equally sized arrays. We also
 * try to minimize clashes in the table by hashing on the method
 * name + number of parameters. Furthermore, we remember whether
 * a name was distinct in the table. If it was, we do not check
 * that the parameters match.
 * <p>
 * The most important methods in terms of performance are
 * lookup() and lookupDefaultMethod().
 */
public final class VTable {
  private final Method[] entries;
  private final Class<?>[][] paramTypes;
  private final boolean[] distinctName;
  private final MethodHandle[] defaultMethods;
  private final int size;
  private final int mask;

  /**
   * Builds the VTable according to the collection of methods.
   * The input for this constructor is created in the Builder
   * class.
   *
   * @param methods               all the methods that need to
   *                              be included in this VTable
   * @param distinctMethodNames   names of methods that have
   *                              not been overloaded
   * @param includeDefaultMethods whether or not we want to
   *                              include default interface
   *                              methods
   */
  private VTable(Collection<Method> methods,
                 Set<String> distinctMethodNames,
                 boolean includeDefaultMethods) {
    this.size = methods.size();
    mask = Math.max(
        (-1 >>> Integer.numberOfLeadingZeros(size * 4 - 1)),
        127);
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

  /**
   * Looks up the method in the VTable.  Returns null if it is
   * not found.
   */
  public Method lookup(Method method) {
    int index = findIndex(method);
    return index < 0 ? null : entries[index];
  }

  /**
   * Looks up the default interface method in the VTable.
   * Returns null if it is not found.
   */
  public MethodHandle lookupDefaultMethod(Method method) {
    int index = findIndex(method);
    return index < 0 ? null : defaultMethods[index];
  }

  /**
   * Returns the number of entries in the VTable.  The size does
   * not change and is fixed at construction.
   */
  public int size() {
    return size;
  }

  /**
   * Returns a stream of Method objects from this VTable.  Used
   * by the ChainedInvocationHandler to verify that all methods
   * in the target have been covered by the various VTables.
   */
  public Stream<Method> stream() {
    return Stream.of(entries).filter(Objects::nonNull);
  }

  /**
   * Returns a stream of Method objects for which we have
   * default interface methods in this VTable.  Used by the
   * ChainedInvocationHandler to verify that all methods in the
   * target have been covered by the various VTables.
   */
  public Stream<Method> streamDefaultMethods() {
    // Heinz: First time I've found a use case for iterate()
    return IntStream.iterate(0, i -> i < entries.length,
        i -> i + 1)
               .filter(i -> defaultMethods[i] != null)
               .mapToObj(i -> entries[i]);
  }

  /**
   * Finds a free position for the entry and then inserts the
   * values into the arrays entries, paramTypes, distinctName,
   * and optionally, defaultMethods.  Duplicate methods are not
   * allowed and will throw an IllegalArgumentException.
   */
  private void put(Method method, boolean distinct,
                   boolean includeDefaultMethods) {
    int index = findIndex(method);
    if (index >= 0)
      throw new IllegalArgumentException(
          "Duplicate method found: " + new MethodKey(method));
    index = ~index; // flip the bits again to find empty space
    entries[index] = method;
    paramTypes[index] = ParameterTypesFetcher.get(method);
    distinctName[index] = distinct;
    if (includeDefaultMethods && method.isDefault()) {
      defaultMethods[index] = getDefaultMethodHandle(method);
    }
  }

  /**
   * Returns a MethodHandle for the default interface method
   * if it is declared and the module is open to our module;
   * null otherwise.
   */
  private MethodHandle getDefaultMethodHandle(Method method) {
    try {
      Class<?> target = method.getDeclaringClass();
      if (isMethodDeclaredInOpenModule(method)) {
        // Thanks Thomas Darimont for this idea
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        return MethodHandles.privateLookupIn(target, lookup)
                   .unreflectSpecial(method, target);
      }
      return null;
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Returns true if the method is in an open module; false
   * otherwise.  For example, if our VTable is inside module
   * "eu.javaspecialists.books.dynamicproxies" and we want to
   * use default methods of interfaces in java.util, we need
   * to explicitly open that module with --add-opens \
   * java.base/java.util=eu.javaspecialists.books.dynamicproxies
   */
  private boolean isMethodDeclaredInOpenModule(Method method) {
    Class<?> target = method.getDeclaringClass();
    String packageName = target.getPackageName();
    Module module = VTable.class.getModule();
    return target.getModule().isOpen(packageName, module);
  }

  /**
   * Returns the index of the method; negative value if it was
   * not found.  Once a method with this name is found, we check
   * if the method was overloaded.  If it was not, then we
   * return the offset immediately.
   */
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
      offset = (offset + 1) & mask;
    }
    // Could not find the method, returning a negative value
    return ~offset;
    // (By flipping the bits again, we know what the first
    // available index in our elements array is)
  }

  /**
   * Returns the initial offset for the method, based on method
   * name and number of parameters.
   */
  private int offset(Method method) {
    return (method.getName().hashCode() +
                method.getParameterCount()) & mask;
  }

  /**
   * Fast array comparison for parameter types.  Classes can be
   * compared with == instead of equals().
   */
  private boolean matches(Class<?>[] types1, Class<?>[] types2) {
    if (types1.length != types2.length) return false;
    for (int i = 0; i < types1.length; i++) {
      if (types1[i] != types2[i]) return false;
    }
    return true;
  }

  public static class Builder {
    private static final BinaryOperator<Method> MOST_SPECIFIC =
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
    private static final Method[] OBJECT_METHODS;
    static {
      try {
        OBJECT_METHODS = new Method[] {
            Object.class.getMethod("toString"),
            Object.class.getMethod("hashCode"),
            Object.class.getMethod("equals", Object.class),
        };
      } catch (NoSuchMethodException e) {
        throw new Error(e);
      }
    }
    private final Map<MethodKey, Method> receiverClassMap;
    private List<Class<?>> targetInterfaces = new ArrayList<>();
    private boolean includeObjectMethods = true;
    private boolean includeDefaultMethods = false;
    private boolean ignoreReturnTypes = false;

    /**
     * @param receiver The class that receives the actual
     *                 method calls.  Does not have to be
     *                 related to the dynamic proxy interfaces.
     */
    public Builder(Class<?> receiver) {
      receiverClassMap = createPublicMethodMap(receiver);
    }

    /**
     * Methods equals(Object), hashCode() and toString() are not
     * added to the VTable.
     */
    public Builder excludeObjectMethods() {
      this.includeObjectMethods = false;
      return this;
    }

    public Builder includeDefaultMethods() {
      this.includeDefaultMethods = true;
      return this;
    }

    public Builder ignoreReturnTypes() {
      this.ignoreReturnTypes = true;
      return this;
    }

    /**
     * One of the target interfaces that we wish to map methods
     * to.
     */
    public Builder addTargetInterface(Class<?> targetIntf) {
      if (!targetIntf.isInterface())
        throw new IllegalArgumentException(
            targetIntf.getCanonicalName() +
                " is not an interface");
      this.targetInterfaces.add(targetIntf);
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
      if (includeObjectMethods) {
        for (Method method : OBJECT_METHODS) {
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
                  MOST_SPECIFIC));

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

      // Lastly we only include those methods that are also in
      // the receiverClassMap and where the return type is
      // assignment compatible with the target method.
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
      var receiverMethod = receiverClassMap.get(entry.getKey());
      if (receiverMethod != null) {
        if (ignoreReturnTypes) return receiverMethod;
        var targetReturn = entry.getValue().getReturnType();
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
          map.merge(key, method, MOST_SPECIFIC);
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