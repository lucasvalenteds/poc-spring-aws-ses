package com.example.user;

public final class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super("None user found with e-mail " + email);
    }
}
