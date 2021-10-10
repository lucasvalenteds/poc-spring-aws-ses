package com.example.user;

import com.example.email.EmailException;
import com.example.email.EmailProperties;
import com.example.email.Postman;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

class UserControllerTest {

    private final Postman postman = Mockito.mock(Postman.class);
    private final WebTestClient webTestClient = WebTestClient
        .bindToController(new UserController(postman))
        .build();

    @Test
    void testCreatingAndDeletingUser() {
        Mockito.when(postman.send(Mockito.any(EmailProperties.class)))
            .thenReturn(Mono.empty());

        webTestClient.post()
            .uri("/users/create")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.USER))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED);

        webTestClient.post()
            .uri("/users/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.CREDENTIALS))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.ACCEPTED);

        Mockito.verify(postman, Mockito.times(2))
            .send(Mockito.any(EmailProperties.class));
    }

    @Test
    void testFailingToCreateUserWithSameEmail() {
        Mockito.when(postman.send(Mockito.any(EmailProperties.class)))
            .thenReturn(Mono.empty());

        webTestClient.post()
            .uri("/users/create")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.USER))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED);

        webTestClient.post()
            .uri("/users/create")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.USER))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        Mockito.verify(postman, Mockito.times(1))
            .send(Mockito.any(EmailProperties.class));
    }

    @Test
    void testFailingToDeleteUnknownUser() {
        webTestClient.post()
            .uri("/users/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.USER))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);

        Mockito.verifyNoInteractions(postman);
    }

    @Test
    void testFailingToSendUserCreatedEmail() {
        Mockito.when(postman.send(UserTestBuilder.USER_CREATED_PROPERTIES))
            .thenReturn(Mono.error(new EmailException("messageId")));

        webTestClient.post()
            .uri("/users/create")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.USER))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.verify(postman)
            .send(UserTestBuilder.USER_CREATED_PROPERTIES);
    }

    @Test
    void testFailingToSendUserDeletedEmail() {
        Mockito.when(postman.send(UserTestBuilder.USER_CREATED_PROPERTIES))
            .thenReturn(Mono.empty());

        Mockito.when(postman.send(UserTestBuilder.USER_DELETED_PROPERTIES))
            .thenReturn(Mono.error(new EmailException("messageId")));

        webTestClient.post()
            .uri("/users/create")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.USER))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED);

        webTestClient.post()
            .uri("/users/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserTestBuilder.USER))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.verify(postman)
            .send(UserTestBuilder.USER_CREATED_PROPERTIES);

        Mockito.verify(postman)
            .send(UserTestBuilder.USER_DELETED_PROPERTIES);
    }
}
