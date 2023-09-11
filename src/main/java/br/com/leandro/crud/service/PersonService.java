package br.com.leandro.crud.service;

import br.com.leandro.crud.exception.ResourceNotFoundException;
import br.com.leandro.crud.model.Person;
import br.com.leandro.crud.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    public Mono<Person> save(Person person) {
        return personRepository.save(person);
    }

    public Mono<Person> findById(Long id) {
        return personRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Could not find person id = " + id)))
                .doOnSuccess(e -> log.error("Success message: {}", e.toString()))
                .doOnError(e -> log.error("Error message: {}", e.getMessage()));
    }

    public Mono<Person> deleteById(Long id) {
        return Mono.defer(() -> Mono.just(id)
                .doOnNext(i -> log.info("Removing id: " + i))
                .flatMap(personRepository::findById)
                .doOnNext(person -> log.info(person.toString()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Could not find person id = " + id)))
                .doOnNext(personRepository::delete)
                .doOnSuccess(e -> log.error("Success message: {}", e.toString()))
                .doOnError(e -> log.error("Error message: {}", e.getMessage())));
    }

    /*public Mono<Person> deleteById(Long id) {
        return findById(id)
                .doOnNext(personRepository::delete);
    }*/


    public Mono<Person> replace(Person newPerson, Long id) {
        return Mono.defer(() -> Mono.just(id)
                .flatMap(personRepository::findById)
                .doOnNext(personRepository::delete)
                .switchIfEmpty(Mono.just(newPerson))
                .flatMap(person -> {
                    person.setId(newPerson.getId());
                    person.setFirstName(newPerson.getFirstName());
                    person.setLastName(newPerson.getLastName());
                    person.setBirthday(newPerson.getBirthday());
                    person.setGender(newPerson.getGender());
                    person.setPersonAddresses(newPerson.getPersonAddresses());
                    person.setPersonImages(newPerson.getPersonImages());
                    return this.save(person);
                })
                .flatMap(this::save)
                .doOnSuccess(e -> log.error("Success message: {}", e.toString()))
                .doOnError(e -> log.error("Error message: {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty()));
    }
}
