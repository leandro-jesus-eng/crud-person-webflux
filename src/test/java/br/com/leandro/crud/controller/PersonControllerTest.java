package br.com.leandro.crud.controller;

import br.com.leandro.crud.model.Person;
import br.com.leandro.crud.service.PersonService;
import br.com.leandro.crud.util.PersonFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class PersonControllerTest {

    @InjectMocks
    private PersonController controller;

    @Mock
    private PersonService personService;

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0); //NOSONAR
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    public void save_CreatesPerson_WhenSuccessful() {
        Person personToBeSaved = PersonFactory.createNewPerson();
        Person personSaved = PersonFactory.createPersonSaved();

        Mockito.when(personService.save(Mockito.any(Person.class)))
                .thenReturn(Mono.just(personSaved));

        StepVerifier.create(controller.save(personToBeSaved))
                .expectSubscription()
                .expectNext(personSaved)
                .verifyComplete();
    }

    @Test
    public void findAll_ReturnFluxOfPerson_WhenSuccessful() {
        Person person = PersonFactory.createNewPerson();

        Person[] arrayArray = List.of(person, person).toArray(new Person[0]);

        Mockito.when(personService.findAll())
                .thenReturn(Flux.just(arrayArray));

        StepVerifier.create(controller.findAll())
                .expectSubscription()
                .expectNext(person, person)
                .verifyComplete();
    }
}