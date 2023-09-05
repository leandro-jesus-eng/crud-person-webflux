package br.com.leandro.crud.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureUtils {

    /**
     * Usage:
     * public void savePeople(Person p1, Person p2) {
     *      var p1Future = CompletableFutureUtils.supplyAsync("saving person", ()->repository.save(p1) );
     *      var p2Future = CompletableFutureUtils.supplyAsync("saving person", ()->repository.save(p2) );
     *      CompletableFuture.allOf(p1Future, p2Future).join();
     *      var p1Updated = p1Future.get();
     *      var p2Updated = p2Future.get();
     * }
     */
    public static <U>CompletableFuture supplyAsync(String identifier, Supplier<U> supplier) {
        return CompletableFuture.supplyAsync( elapsedTime(identifier, supplier) );
    }

    /**
     * Usage:
     * public Person savePerson(Person p) {
     *      return CompletableFutureUtils.elapsedTime("saving person", ()->repository.save(p) ).get();
     * }
     *
     * @param identifier
     * @param supplier
     * @return
     * @param <T>
     */
    public static <T> Supplier elapsedTime(String identifier, Supplier<T> supplier) {
        return () -> {
            final long begin = System.currentTimeMillis();
            T result = supplier.get();
            final long end = System.currentTimeMillis()-begin;

            System.out.println("Elapsed time of ("+identifier+"): "+end+"ms");

            return result;
        };
    }
}
