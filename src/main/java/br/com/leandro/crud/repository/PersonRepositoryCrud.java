package br.com.leandro.crud.repository;

import br.com.leandro.crud.model.Person;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;

@Component
public class PersonRepositoryCrud implements PersonRepository<Person, Long> {

    private Set<Person> database;
    private Long identity;

    @PostConstruct
    public void init() {
        database = new HashSet();
        identity = 0L;

        populate();
    }

    protected void populate() {
        for( int i=0 ; i<10 ; i++) {
            var person = Person.builder()
                    .id(++identity)
                    .firstName("firtName"+identity)
                    .lastName("lastName"+identity)
                    .build();
            save(person);
        }
    }

    @Override
    public Mono<Person> save(Person entity) {
        if( Objects.isNull(entity.getId()) )
            entity.setId(++identity);

        database.add(entity);
        return Mono.just(entity);
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(database.toArray(new Person[0]));
    }

    @Override
    public Flux<Person> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public Flux<Person> findAllById(Publisher<Long> idStream) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> delete(Person entity) {
        database.remove(entity);
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteById(Long aLong) {
        database.remove(Person.builder().id(aLong).build());
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteAllById(Iterable<? extends Long> longs) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends Person> entities) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends Person> entityStream) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }



    @Override
    public Mono<Void> deleteById(Publisher<Long> id) {
        return null;
    }

    @Override
    public <S extends Person> Flux<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Person> Flux<S> saveAll(Publisher<S> entityStream) {
        return null;
    }

    @Override
    public Mono<Person> findById(Long aLong) {
        var person = Person.builder().id(aLong).build();

        if ( database.contains(person) ) {
            return Mono.just(
                    database.parallelStream()
                        .filter( p -> p.getId().equals(aLong) )
                        .findFirst().get());
        }

        return Mono.empty();
    }

    @Override
    public Mono<Person> findById(Publisher<Long> id) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Long aLong) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<Long> id) {
        return null;
    }
}
