package com.example.email;

import reactor.core.publisher.Mono;

public interface Email {

    boolean accepts(EmailProperties properties);

    Mono<EmailComposed> compose(EmailProperties properties);
}
