package br.com.leandro.crud.controller;

import br.com.leandro.crud.model.Person;
import br.com.leandro.crud.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Return all registered people")
    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("hello!");
    }


    @Operation(summary = "Return all registered people")
    @GetMapping
    public Flux<Person> findAll() {
        return personService.findAll();
    }

    @Operation(summary = "Save or update a person")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Person> save(@RequestBody Person person) {
        return personService.save(person);
    }

    @Operation(summary = "Return a person by Id")
    @GetMapping("/{id}")
    public Mono<Person> findById(@PathVariable Long id) {
        return personService.findById(id);
    }

    @Operation(summary = "Replace a person by Id")
    @PutMapping("{id}")
    public Mono<Person> replacePerson(@RequestBody Person newPerson, @PathVariable Long id) {
        return personService.replace(newPerson, id);
    }

    @Operation(summary = "Delete a person by Id")
    @DeleteMapping("{id}")
    public Mono<Person> delete(@PathVariable Long id) {
        return personService.deleteById(id);
    }
}