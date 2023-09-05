package br.com.leandro.crud.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Partition<T> extends AbstractList<List<T>> {

    private final List<T> list;

    private final int chunkSize;

    public static final int CHUNCK_SIZE_DEFAULT = 2000;

    public Partition(List<T> list, int chunkSize) {
        this.list = new ArrayList<>(list);
        this.chunkSize = chunkSize;
    }

    public static <T> Partition<T> ofSize(List<T> list, int chunkSize) {
        return new Partition<>(list, chunkSize);
    }

    @Override
    public List<T> get(int index) {
        int start = index * chunkSize;
        int end = Math.min(start + chunkSize, list.size());

        if(start > end) {
            throw new IndexOutOfBoundsException("Index "+index+" is out of the list range <0, "+(size()-1)+">");
        }

        return new ArrayList<>(list.subList(start, end));
    }

    @Override
    public int size() {
        return (int) Math.ceil( (double) list.size()/(double)chunkSize );
    }

    /**
     * Usage:
     * public List<Person> saveAll(List personList) {
     *      // saves the objects in a partitioned way and returns the updated complete list
     *      return Partition.runPartitioned( personList, personListChuncked -> repository.saveAll(personListChuncked) )
     * }
     *
     * @param fullList
     * @param chunkSize
     * @param f
     * @return
     */
    public static <T,R> List<R> runPartitioned (List<T> fullList, int chunkSize, Function<List<T>, List<R>> f) {
        return Partition.ofSize(fullList, chunkSize)
                .stream()
                .map(listChunked -> f.apply(listChunked))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static <T,R> List<R> runPartitioned (List<T> fullList,  Function<List<T>, List<R>> f) {
        return runPartitioned(fullList, CHUNCK_SIZE_DEFAULT, f);
    }

    public static <T,R> List<R> runPartitionedParallel (List<T> fullList, int chunkSize, Function<List<T>, List<R>> f) {
        return Partition.ofSize(fullList, chunkSize)
                .parallelStream()
                .map(f)// listChunked -> f.apply(listChunked) or f::apply
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static <T,R> List<R> runPartitionedParallel (List<T> fullList,  Function<List<T>, List<R>> f) {
        return runPartitionedParallel(fullList, CHUNCK_SIZE_DEFAULT, f);
    }


}
