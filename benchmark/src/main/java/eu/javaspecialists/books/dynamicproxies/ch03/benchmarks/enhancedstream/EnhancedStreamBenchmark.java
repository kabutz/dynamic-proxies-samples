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

package eu.javaspecialists.books.dynamicproxies.ch03.benchmarks.enhancedstream;

import eu.javaspecialists.books.dynamicproxies.samples.ch03.enhancedstream.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.*;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

// tag::listing[]
@Fork(3)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)

public class EnhancedStreamBenchmark {
  public static class Beach {
    public final static String[] ELEMENTS =
        {"Kalathas", "Stavros", "STAVROS",
            "marathi", "kalathas", "baLos", "Balos"};
    // case insensitive hashCode() and equals()
    public static final ToIntFunction<String> HASH_CODE =
        s -> s.toUpperCase().hashCode();
    public static final BiPredicate<String, String> EQUALS =
        (s1, s2) ->
            s1.toUpperCase().equals(s2.toUpperCase());
    // keep the string with the highest total ascii value
    public static final BinaryOperator<String> MERGE =
        (s1, s2) ->
            s1.chars().sum() < s2.chars().sum() ? s2 : s1;
  }
  public static class Methods {
    public final static Method[] ELEMENTS =
        ArrayDeque.class.getMethods();
    public static final ToIntFunction<Method> HASH_CODE =
        method -> method.getName().hashCode() +
                      method.getParameterCount();

    public static final BiPredicate<Method, Method> EQUALS =
        (method1, method2) ->
            method1.getName().equals(method2.getName()) &&
                method1.getParameterCount() ==
                    method2.getParameterCount() &&
                Arrays.equals(method1.getParameterTypes(),
                    method2.getParameterTypes());

    public static final BinaryOperator<Method> MERGE =
        (method1, method2) -> {
          if (method1.getReturnType()
                  .isAssignableFrom(method2.getReturnType()))
            return method2;
          if (method2.getReturnType()
                  .isAssignableFrom(method1.getReturnType()))
            return method1;
          throw new IllegalArgumentException(
              "Conflicting return types " +
                  method1.getReturnType().getCanonicalName() +
                  " and " +
                  method2.getReturnType().getCanonicalName());
        };
  }

  private static <T> Stream<T> createEnhancedStreamStatic(
      ToIntFunction<T> hashCode, BiPredicate<T, T> equals,
      BinaryOperator<T> merger, T... elements) {
    return EnhancedStreamStatic.of(elements)
               .distinct(hashCode, equals, merger);
  }

  private static <T> Stream<T> createEnhancedStreamDynamic(
      ToIntFunction<T> hashCode, BiPredicate<T, T> equals,
      BinaryOperator<T> merger, T... elements) {
    return EnhancedStream.of(elements)
               .distinct(hashCode, equals, merger);
  }

  private static <T> Stream<T> createEnvelopedStream(
      ToIntFunction<T> hashCode, BiPredicate<T, T> equals,
      BinaryOperator<T> merger, T... elements) {
    return Stream.of(elements)
               .map(s -> new Envelope<>(s, hashCode,
                   equals, merger))
               .collect((Collectors.toCollection(
                   LinkedHashSet::new)))
               .stream()
               .map(Envelope::merge);
  }


  @Benchmark
  public String beachesStatic() {
    return createEnhancedStreamStatic(
        Beach.HASH_CODE, Beach.EQUALS, Beach.MERGE,
        Beach.ELEMENTS).collect(Collectors.joining(", "));
  }
  @Benchmark
  public String beachesDynamic() {
    return createEnhancedStreamDynamic(
        Beach.HASH_CODE, Beach.EQUALS, Beach.MERGE,
        Beach.ELEMENTS).collect(Collectors.joining(", "));
  }
  @Benchmark
  public String beachesEnvelope() {
    return createEnvelopedStream(
        Beach.HASH_CODE, Beach.EQUALS, Beach.MERGE,
        Beach.ELEMENTS).collect(Collectors.joining(", "));
  }


  @Benchmark
  public long methodsStatic() {
    return createEnhancedStreamStatic(
        Methods.HASH_CODE, Methods.EQUALS, Methods.MERGE,
        Methods.ELEMENTS).count();
  }
  @Benchmark
  public long methodsDynamic() {
    return createEnhancedStreamDynamic(
        Methods.HASH_CODE, Methods.EQUALS, Methods.MERGE,
        Methods.ELEMENTS).count();
  }
  @Benchmark
  public long methodsEnvelope() {
    return createEnvelopedStream(
        Methods.HASH_CODE, Methods.EQUALS, Methods.MERGE,
        Methods.ELEMENTS).count();
  }


  private static class EnhancedStreamStatic<T> implements Stream<T> {
    private static final class Key<E> {
      private final E e;
      private final ToIntFunction<E> hashCode;
      private final BiPredicate<E, E> equals;

      public Key(E e, ToIntFunction<E> hashCode,
                 BiPredicate<E, E> equals) {
        this.e = e;
        this.hashCode = hashCode;
        this.equals = equals;
      }

      public int hashCode() {
        return hashCode.applyAsInt(e);
      }

      public boolean equals(Object obj) {
        if (!(obj instanceof Key)) return false;
        @SuppressWarnings("unchecked")
        Key<E> that = (Key<E>) obj;
        return equals.test(this.e, that.e);
      }
    }

    private Stream<T> delegate;

    public EnhancedStreamStatic(Stream<T> delegate) {
      this.delegate = delegate;
    }

    public EnhancedStreamStatic<T> distinct(ToIntFunction<T> hashCode,
                                            BiPredicate<T, T> equals,
                                            BinaryOperator<T> merger) {
      delegate = collect(Collectors.toMap(
          t -> new Key<>(t, hashCode, equals),
          Function.identity(),
          merger,
          LinkedHashMap::new))
                     // thanks Federico Peralta Schaffner for
                     // suggesting that
                     // we use a LinkedHashMap.  That way we
                     // can preserve the
                     // original order and get the speed of
                     // hashing.
                     .values()
                     .stream();
      return this;
    }

    public EnhancedStreamStatic<T> filter(
        Predicate<? super T> predicate) {
      this.delegate = delegate.filter(predicate);
      return this;
    }

    public <R> EnhancedStreamStatic<R> map(
        Function<? super T, ? extends R> mapper) {
      return new EnhancedStreamStatic<>(delegate.map(mapper));
    }

    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
      return delegate.mapToInt(mapper);
    }

    public LongStream mapToLong(
        ToLongFunction<? super T> mapper) {
      return delegate.mapToLong(mapper);
    }

    public DoubleStream mapToDouble(
        ToDoubleFunction<? super T> mapper) {
      return delegate.mapToDouble(mapper);
    }

    public <R> EnhancedStreamStatic<R> flatMap(
        Function<? super T,
                    ? extends Stream<? extends R>> mapper) {
      return new EnhancedStreamStatic<>(delegate.flatMap(mapper));
    }

    public IntStream flatMapToInt(
        Function<? super T, ? extends IntStream> mapper) {
      return delegate.flatMapToInt(mapper);
    }

    public LongStream flatMapToLong(
        Function<? super T, ? extends LongStream> mapper) {
      return delegate.flatMapToLong(mapper);
    }

    public DoubleStream flatMapToDouble(
        Function<? super T, ? extends DoubleStream> mapper) {
      return delegate.flatMapToDouble(mapper);
    }

    public EnhancedStreamStatic<T> distinct() {
      delegate = delegate.distinct();
      return this;
    }

    public EnhancedStreamStatic<T> sorted() {
      delegate = delegate.sorted();
      return this;
    }

    public EnhancedStreamStatic<T> sorted(
        Comparator<? super T> comparator) {
      delegate = delegate.sorted(comparator);
      return this;
    }

    public EnhancedStreamStatic<T> peek(Consumer<? super T> action) {
      delegate = delegate.peek(action);
      return this;
    }

    public EnhancedStreamStatic<T> limit(long maxSize) {
      delegate = delegate.limit(maxSize);
      return this;
    }

    public EnhancedStreamStatic<T> skip(long n) {
      delegate = delegate.skip(n);
      return this;
    }

    public EnhancedStreamStatic<T> takeWhile(
        Predicate<? super T> predicate) {
      delegate = delegate.takeWhile(predicate);
      return this;
    }

    public EnhancedStreamStatic<T> dropWhile(
        Predicate<? super T> predicate) {
      delegate = delegate.dropWhile(predicate);
      return this;
    }

    public void forEach(Consumer<? super T> action) {
      delegate.forEach(action);
    }

    public void forEachOrdered(Consumer<? super T> action) {
      delegate.forEachOrdered(action);
    }

    public Object[] toArray() {
      return delegate.toArray();
    }

    public <A> A[] toArray(IntFunction<A[]> generator) {
      return delegate.toArray(generator);
    }

    public T reduce(T identity, BinaryOperator<T> accumulator) {
      return delegate.reduce(identity, accumulator);
    }

    public Optional<T> reduce(BinaryOperator<T> accumulator) {
      return delegate.reduce(accumulator);
    }

    public <U> U reduce(U identity,
                        BiFunction<U, ? super T, U> accumulator,
                        BinaryOperator<U> combiner) {
      return delegate.reduce(identity, accumulator, combiner);
    }

    public <R> R collect(Supplier<R> supplier,
                         BiConsumer<R, ? super T> accumulator,
                         BiConsumer<R, R> combiner) {
      return delegate.collect(supplier, accumulator, combiner);
    }

    public <R, A> R collect(
        Collector<? super T, A, R> collector) {
      return delegate.collect(collector);
    }

    public Optional<T> min(Comparator<? super T> comparator) {
      return delegate.min(comparator);
    }

    public Optional<T> max(Comparator<? super T> comparator) {
      return delegate.max(comparator);
    }

    public long count() {
      return delegate.count();
    }

    public boolean anyMatch(Predicate<? super T> predicate) {
      return delegate.anyMatch(predicate);
    }

    public boolean allMatch(Predicate<? super T> predicate) {
      return delegate.allMatch(predicate);
    }

    public boolean noneMatch(Predicate<? super T> predicate) {
      return delegate.noneMatch(predicate);
    }

    public Optional<T> findFirst() {
      return delegate.findFirst();
    }

    public Optional<T> findAny() {
      return delegate.findAny();
    }

    public Iterator<T> iterator() {
      return delegate.iterator();
    }

    public Spliterator<T> spliterator() {
      return delegate.spliterator();
    }

    public boolean isParallel() {
      return delegate.isParallel();
    }

    public EnhancedStreamStatic<T> sequential() {
      delegate = delegate.sequential();
      return this;
    }

    public EnhancedStreamStatic<T> parallel() {
      delegate = delegate.parallel();
      return this;
    }

    public EnhancedStreamStatic<T> unordered() {
      delegate = delegate.unordered();
      return this;
    }

    public EnhancedStreamStatic<T> onClose(Runnable closeHandler) {
      delegate = delegate.onClose(closeHandler);
      return this;
    }

    public void close() {
      delegate.close();
    }

    public static <T> EnhancedStreamStatic<T> of(T t) {
      return new EnhancedStreamStatic<>(Stream.of(t));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    // Creating a stream from an array is safe
    public static <T> EnhancedStreamStatic<T> of(T... values) {
      return new EnhancedStreamStatic<>(Arrays.stream(values));
    }
  }
  private static class Envelope<E> {
    private final E e;
    private final ToIntFunction<E> hashCode;
    private final BiPredicate<E, E> equals;
    private final BinaryOperator<E> merge;
    private final Collection<E> others =
        Collections.newSetFromMap(new IdentityHashMap<>());

    public Envelope(E e,
                    ToIntFunction<E> hashCode,
                    BiPredicate<E, E> equals,
                    BinaryOperator<E> merge) {
      this.e = e;
      this.hashCode = hashCode;
      this.equals = equals;
      this.merge = merge;
    }

    public E merge() {
      E merged = e;
      for (E other : others) {
        merged = merge.apply(merged, other);
      }
      return merged;
    }

    @Override
    public int hashCode() {
      return hashCode.applyAsInt(e);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Envelope)) return false;
      Envelope<E> that = (Envelope<E>) obj;
      var b = equals.test(this.e, that.e);
      if (b) {
        this.others.add(that.e);
        that.others.add(this.e);
      }
      return b;
    }
    @Override
    public String toString() {
      return "Envelope{e='" + e + '\'' + " " + others + '}';
    }
  }

  public static void main(String... args) throws RunnerException {
    Options opt = new OptionsBuilder()
                      .include(MethodHandles.lookup().lookupClass().getName())
                      .forks(1)
                      .warmupIterations(5)
                      .warmupTime(TimeValue.seconds(1))
                      .measurementIterations(10)
                      .measurementTime(TimeValue.seconds(1))
                      .addProfiler("gc")
                      .build();
    new Runner(opt).run();
  }
}
// end::listing[]
