package br.com.leandro.crud.repository;

import br.com.leandro.crud.model.Person;
import org.reactivestreams.Publisher;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {

    Mono<Person> save(Person entity);

    Flux<Person> findAll();

    Flux<Person> findAllById(Iterable<Long> longs);

    Flux<Person> findAllById(Publisher<Long> idStream);
}
