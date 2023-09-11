package br.com.leandro.crud.repository;

import br.com.leandro.crud.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository // extends ReactiveCrudRepository<Person, Long>
{

    Mono<Person> save(Person entity);

    Flux<Person> findAll();

    Mono<Person> findById(Long aLong);

    Mono<Void> delete(Person entity);

    Mono<Void> deleteById(Long aLong);
}