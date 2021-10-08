package com.example.email;

import reactor.core.publisher.Mono;

import java.util.List;

public abstract class Postman {

    protected final List<Email> emails;

    protected Postman(List<Email> emails) {
        this.emails = emails;
    }

    public abstract Mono<Void> send(EmailProperties properties);
}