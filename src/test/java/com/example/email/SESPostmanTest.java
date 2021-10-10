package com.example.email;

import com.example.ThymeleafConfiguration;
import com.example.testing.TestingConfiguration;
import com.example.user.UserCreatedEmail;
import com.example.user.UserDeletedEmail;
import com.example.user.UserTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;
import software.amazon.awssdk.services.ses.model.VerifyEmailAddressRequest;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig({TestingConfiguration.class, ThymeleafConfiguration.class})
class SESPostmanTest {

    private ISpringWebFluxTemplateEngine templateEngine;
    private Path templatesDirectory;

    @BeforeEach
    public void beforeEach(ApplicationContext context) {
        templateEngine = context.getBean(ISpringWebFluxTemplateEngine.class);
        templatesDirectory = context.getBean(Path.class);
    }

    @Test
    void testSendingEmail() {
        var emails = List.<Email>of(new UserCreatedEmail(templateEngine, templatesDirectory));
        var sesClient = Mockito.mock(SesAsyncClient.class);
        var source = SESPostmanTestBuilder.SOURCE;
        var postman = new SESPostman(emails, sesClient, source);

        var sesResponse = SESPostmanTestBuilder.createSesSuccessfulSendEmailResponse();
        Mockito.when(sesClient.sendEmail(Mockito.any(SendEmailRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(sesResponse));

        var send = postman.send(UserTestBuilder.USER_CREATED_PROPERTIES);

        StepVerifier.create(send)
            .verifyComplete();

        Mockito.verify(sesClient, Mockito.times(1))
            .sendEmail(Mockito.any(SendEmailRequest.class));
    }

    @Test
    void testFailingToFindEmailHandler() {
        var emails = List.<Email>of(new UserDeletedEmail(templateEngine, templatesDirectory));
        var sesClient = Mockito.mock(SesAsyncClient.class);
        var source = SESPostmanTestBuilder.SOURCE;
        var postman = new SESPostman(emails, sesClient, source);

        var send = postman.send(UserTestBuilder.USER_CREATED_PROPERTIES);

        StepVerifier.create(send)
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(EmailException.class)
                    .hasMessageEndingWith("None e-mail handler found for properties of type UserCreatedProperties")
            )
            .verify();

        Mockito.verifyNoInteractions(sesClient);
    }

    @Test
    void testHandlingSdkError() {
        var emails = List.<Email>of(new UserCreatedEmail(templateEngine, templatesDirectory));
        var sesClient = Mockito.mock(SesAsyncClient.class);
        var source = SESPostmanTestBuilder.SOURCE;
        var postman = new SESPostman(emails, sesClient, source);

        Mockito.when(sesClient.sendEmail(Mockito.any(SendEmailRequest.class)))
            .thenReturn(CompletableFuture.failedFuture(SesException.create("SES Exception", null)));

        var send = postman.send(UserTestBuilder.USER_CREATED_PROPERTIES);

        StepVerifier.create(send)
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(EmailException.class)
                    .hasCauseInstanceOf(SdkException.class)
                    .hasMessageEndingWith("SES Exception")
            )
            .verify();

        Mockito.verify(sesClient, Mockito.times(1))
            .sendEmail(Mockito.any(SendEmailRequest.class));
    }

    @Test
    void testHandlingErrorWhileSending() {
        var emails = List.<Email>of(new UserCreatedEmail(templateEngine, templatesDirectory));
        var sesClient = Mockito.mock(SesAsyncClient.class);
        var source = SESPostmanTestBuilder.SOURCE;
        var postman = new SESPostman(emails, sesClient, source);

        var sesResponse = SESPostmanTestBuilder.createSesFailureSendEmailResponse();
        Mockito.when(sesClient.sendEmail(Mockito.any(SendEmailRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(sesResponse));

        var send = postman.send(UserTestBuilder.USER_CREATED_PROPERTIES);

        StepVerifier.create(send)
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(EmailException.class)
                    .hasMessage("Could not send e-mail via SES (messageId=%s)", sesResponse.messageId())
            )
            .verify();

        Mockito.verify(sesClient, Mockito.times(1))
            .sendEmail(Mockito.any(SendEmailRequest.class));
    }


    @Test
    void testVerifyingSourceEmail() {
        var emails = List.<Email>of();
        var sesClient = Mockito.mock(SesAsyncClient.class);
        var source = SESPostmanTestBuilder.SOURCE;
        var postman = new SESPostman(emails, sesClient, source);

        var sesResponse = SESPostmanTestBuilder.createSesSuccessfulVerifyEmailAddressResponse();
        Mockito.when(sesClient.verifyEmailAddress(Mockito.any(VerifyEmailAddressRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(sesResponse));

        var verify = postman.verifySourceEmail();

        StepVerifier.create(verify)
            .verifyComplete();
    }

    @Test
    void testFailingToVerifySourceEmail() {
        var emails = List.<Email>of();
        var sesClient = Mockito.mock(SesAsyncClient.class);
        var source = SESPostmanTestBuilder.SOURCE;
        var postman = new SESPostman(emails, sesClient, source);

        var sesResponse = SESPostmanTestBuilder.createSesFailureVerifyEmailAddressResponse();
        Mockito.when(sesClient.verifyEmailAddress(Mockito.any(VerifyEmailAddressRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(sesResponse));

        var verify = postman.verifySourceEmail();

        StepVerifier.create(verify)
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(EmailException.class)
            )
            .verify();
    }
}