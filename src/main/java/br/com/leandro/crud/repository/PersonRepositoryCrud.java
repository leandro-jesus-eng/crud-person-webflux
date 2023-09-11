package br.com.leandro.crud.repository;

import br.com.leandro.crud.model.Person;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class PersonRepositoryCrud implements PersonRepository {

    private Set<Person> database;

    private Long identity;

    @PostConstruct
    public void init() {
        database = new HashSet();
        identity = 0L;

        populate();
    }

    protected void populate() {
        for (int i = 0; i < 10; i++) {
            var person = Person.builder()
                    .id(++identity)
                    .firstName("firtName" + identity)
                    .lastName("lastName" + identity)
                    .build();
            save(person);
        }
    }

    @Override
    public Mono<Person> save(Person entity) {
        if (Objects.isNull(entity.getId()))
            entity.setId(++identity);

        database.remove(entity);
        database.add(entity);

        return Mono.just(entity);
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(database.toArray(new Person[0]));
    }

    @Override
    public Mono<Person> findById(Long aLong) {
        var person = Person.builder().id(aLong).build();

        if (database.contains(person)) {
            return Mono.just(
                    database.stream()
                            .filter(p -> p.getId().equals(aLong))
                            .findFirst().get());
        }

        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Person entity) {
        database.remove(entity);
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteById(Long aLong) {
        return delete(Person.builder().id(aLong).build());
    }
}
