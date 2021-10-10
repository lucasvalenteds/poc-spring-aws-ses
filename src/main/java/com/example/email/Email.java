package com.example.email;

import com.example.user.UserCreatedEmail;
import com.example.user.UserDeletedEmail;
import reactor.core.publisher.Mono;

public sealed interface Email
    permits UserCreatedEmail, UserDeletedEmail {

    boolean accepts(EmailProperties properties);

    Mono<EmailComposed> compose(EmailProperties properties);
}
