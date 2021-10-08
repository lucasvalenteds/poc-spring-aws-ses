package com.example.email;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;

public final class SESPostman extends Postman {

    private final SesAsyncClient client;
    private final String source;

    public SESPostman(List<Email> emails, SesAsyncClient client, String source) {
        super(emails);
        this.client = client;
        this.source = source;
    }

    @Override
    public Mono<Void> send(EmailProperties properties) {
        return Flux.fromIterable(this.emails)
            .filter(it -> it.accepts(properties)).singleOrEmpty()
            .switchIfEmpty(Mono.error(new EmailException(properties)))
            .flatMap(email -> email.compose(properties))
            .flatMap(emailComposed -> {
                var destination = Destination.builder()
                    .toAddresses(emailComposed.targets())
                    .build();

                var message = Message.builder()
                    .subject(Content.builder()
                        .charset(StandardCharsets.UTF_8.displayName())
                        .data(emailComposed.subject())
                        .build())
                    .body(Body.builder()
                        .html(Content.builder()
                            .charset(StandardCharsets.UTF_8.displayName())
                            .data(emailComposed.body())
                            .build())
                        .build())
                    .build();

                var request = SendEmailRequest.builder()
                    .source(this.source)
                    .destination(destination)
                    .message(message)
                    .build();

                return Mono.fromFuture(() -> client.sendEmail(request))
                    .subscribeOn(Schedulers.boundedElastic())
                    .onErrorMap(EmailException::new);
            })
            .flatMap(response -> {
                if (response.sdkHttpResponse().isSuccessful()) {
                    return Mono.empty();
                } else {
                    return Mono.error(new EmailException(response.messageId()));
                }
            });
    }
}
