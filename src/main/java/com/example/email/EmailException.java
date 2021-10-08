package com.example.email;

public final class EmailException extends RuntimeException {

    public EmailException(Throwable throwable) {
        super(throwable);
    }

    public EmailException(EmailProperties properties) {
        super("None e-mail handler found for properties of type " + properties.getClass().getSimpleName());
    }

    public EmailException(String messageId) {
        super("Could not send e-mail via SES (messageId=" + messageId + ")");
    }
}
