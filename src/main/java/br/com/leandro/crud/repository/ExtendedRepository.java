package br.com.leandro.crud.repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ExtendedRepository<T, ID> {

    <S extends T> S persist(S entity);

    <S extends T> List<S> persistAll(Iterable<S> entities);

    <S extends T> S merge(S entity);

    <S extends T> List<S> mergeAll(Iterable<S> entities);
}
