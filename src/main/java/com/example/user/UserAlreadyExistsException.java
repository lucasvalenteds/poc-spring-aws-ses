package com.example.user;

public final class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(User user) {
        super("User already exists with e-mail " + user.getEmail());
    }
}