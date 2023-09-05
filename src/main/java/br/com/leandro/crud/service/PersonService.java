package br.com.leandro.crud.service;

import br.com.leandro.crud.controller.ResourceNotFoundException;
import br.com.leandro.crud.model.Person;
import br.com.leandro.crud.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
	
	public void deleteById(Long id) {
		personRepository.deleteById(id);
	}

	public Mono<Person> findById(Long id) {
		return personRepository
				.findById(id)
				.switchIfEmpty( Mono.error(new ResourceNotFoundException("Could not find person id = "+id)) );
	}
	
	public Mono<Person> replace(Person newPerson, Long id) {
		return Mono.just(id)
				.flatMap(personRepository::findById)
				.flatMap(person -> {
					if (Objects.nonNull(person)) {
						person.setFirstName(newPerson.getFirstName());
						person.setLastName(newPerson.getLastName());
						person.setBirthday(newPerson.getBirthday());
						person.setGender(newPerson.getGender());
						person.setPersonAddresses(newPerson.getPersonAddresses());
						person.setPersonImages(newPerson.getPersonImages());
						return personRepository.save(person);
					}

					newPerson.setId(id);
					return personRepository.save(newPerson);
                });
	}

	public Mono<Person> replace2(Person newPerson, Long id) {
		return Mono.just(id)
				.flatMap(personRepository::findById)
				.doOnNext(person -> {
					//var person = (Person)person1;
					if (Objects.nonNull(person)) {
						person.setFirstName(newPerson.getFirstName());
						person.setLastName(newPerson.getLastName());
						person.setBirthday(newPerson.getBirthday());
						person.setGender(newPerson.getGender());
						person.setPersonAddresses(newPerson.getPersonAddresses());
						person.setPersonImages(newPerson.getPersonImages());
						personRepository.save(person);
					}

					newPerson.setId(id);
					personRepository.save(newPerson);
				})
				.doOnSuccess(e -> log.error("Success message: {}", e.toString()))
				.doOnError(e -> log.error("Error message: {}", e.getMessage()))
				.onErrorResume(e -> Mono.empty());
	}
}
