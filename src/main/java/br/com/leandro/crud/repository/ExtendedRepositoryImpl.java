package br.com.leandro.crud.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExtendedRepositoryImpl<T, ID> implements ExtendedRepository<T, ID> {

    private final EntityManager entityManager;

    @Override
    public <S extends T> S persist(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> List<S> persistAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null.");
        List<S> result = new ArrayList<>();
        entities.forEach( e -> result.add( persist(e) ) );
        return result;
    }

    @Override
    public <S extends T> S merge(S entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public <S extends T> List<S> mergeAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null.");
        List<S> result = new ArrayList<>();
        entities.forEach( e -> result.add( merge(e) ) );
        return result;
    }
}
