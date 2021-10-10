package com.example.user;

public final class UserTestBuilder {

    public static final Credentials CREDENTIALS = new Credentials("john.smith@foocompany.com", "123456");

    public static final User USER = new User(CREDENTIALS.getEmail(), CREDENTIALS.getPassword(), "John", "Smith");

    public static final UserCreatedProperties USER_CREATED_PROPERTIES = new UserCreatedProperties(USER);

    private UserTestBuilder() {
    }
}
