package br.com.leandro.crud.integration;

import br.com.leandro.crud.model.Person;
import br.com.leandro.crud.util.PersonFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonIntegrationTests {
    @Autowired
    WebTestClient client;

    static final String BASE_URI = "/person";

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install(
                builder -> builder.allowBlockingCallsInside("java.util.UUID", "randomUUID")
                        .allowBlockingCallsInside("java.io.FilterInputStream", "read")
                        .allowBlockingCallsInside("java.io.InputStream", "readNBytes")
                        //.allowBlockingCallsInside("br.com.leandro.crud.repository.PersonRepositoryCrud", "findById")
                        //.allowBlockingCallsInside("br.com.leandro.crud.repository.PersonRepository", "findById")

        );
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    @Order(0)
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
    @Order(1)
    public void listAll_ReturnFluxOfPerson_WhenSuccessful() {
        var person = Person.builder()
                .id(1L)
                .firstName("firtName1")
                .lastName("lastName1")
                .build();

        client
                .get()
                .uri(BASE_URI)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Person.class)
                .hasSize(10)
                .contains(person);
    }

    @Test
    @Order(2)
    public void findById_ReturnMonoPerson_WhenSuccessful() {
        var person = Person.builder()
                .id(1L)
                .firstName("firtName1")
                .lastName("lastName1")
                .build();

        client
                .get()
                .uri(BASE_URI+"/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(person);
    }

    @Test
    @Order(3)
    public void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        client
                .get()
                .uri(BASE_URI+"/{id}", 99)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Could not find person id = 99");
    }

    @Test
    @Order(4)
    public void save_CreatesPerson_WhenSuccessful() {
        Person personToBeSaved = PersonFactory.createNewPerson();

        Person personSaved = PersonFactory.createNewPerson();
        personSaved.setId(11L);

        EntityExchangeResult<Person> personEntityExchangeResult = client
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personToBeSaved))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Person.class)
                .isEqualTo(personSaved)
                .returnResult();

        Person responseBody = personEntityExchangeResult.getResponseBody();
        Assertions.assertEquals(personSaved, responseBody);
    }

}
