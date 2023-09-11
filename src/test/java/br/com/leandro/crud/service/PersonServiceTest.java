package br.com.leandro.crud.service;

import br.com.leandro.crud.exception.ResourceNotFoundException;
import br.com.leandro.crud.model.Person;
import br.com.leandro.crud.repository.PersonRepository;
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

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

// Junit5
@ExtendWith(SpringExtension.class)
class PersonServiceTest {

    @InjectMocks
    PersonService service;

    @Mock
    PersonRepository personRepository;

    // inicia o block hound para verificar se existem threads bloqueadas
    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    // verifica se o block hound está funcionando
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
    public void findAll_ReturnFluxOfPerson_WhenSuccessful() {
        Person person = PersonFactory.createNewPerson();

        Mockito.when(personRepository.findAll())
                .thenReturn(Flux.just(person));

        StepVerifier.create(service.findAll())
                .expectSubscription()
                .expectNext(person)
                .verifyComplete();
    }

    @Test
    public void findById_ReturnMonoPerson_WhenSuccessful() {
        Person person = PersonFactory.createPersonSaved();

        Mockito.when(personRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(person));

        StepVerifier.create(service.findById(1L))
                .expectSubscription()
                .expectNext(person)
                .verifyComplete();
    }

    @Test
    public void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        Mockito.when(personRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.findById(2L))
                .expectSubscription()
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    public void save_CreatesPerson_WhenSuccessful() {
        Person personSaved = PersonFactory.createPersonSaved();
        Person personToBeSaved = PersonFactory.createNewPerson();

        Mockito.when(personRepository.save(Mockito.any(Person.class)))
                .thenReturn(Mono.just(personSaved));

        StepVerifier.create(service.save(personToBeSaved))
                .expectSubscription()
                .expectNext(personSaved)
                .verifyComplete();
    }

    @Test
    void deleteById() {
    }

    @Test
    void replace() {
    }
}