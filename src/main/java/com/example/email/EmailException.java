package com.example.email;

public final class EmailException extends RuntimeException {

    public EmailException(String message) {
        super(message);
    }

    public EmailException(Throwable throwable) {
        super(throwable);
    }

    public EmailException(EmailProperties properties) {
        super("None e-mail handler found for properties of type " + properties.getClass().getSimpleName());
    }
}
