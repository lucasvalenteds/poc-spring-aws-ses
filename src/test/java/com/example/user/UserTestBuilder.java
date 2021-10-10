package com.example.user;

public final class UserTestBuilder {

    public static final Credentials CREDENTIALS = new Credentials("john.smith@foocompany.com", "123456");

    public static final User USER = new User(CREDENTIALS.email(), CREDENTIALS.password(), "John", "Smith");

    public static final UserCreatedProperties USER_CREATED_PROPERTIES = new UserCreatedProperties(USER);

    public static final UserDeletedProperties USER_DELETED_PROPERTIES =
        new UserDeletedProperties(USER.email(), USER.firstname());

    private UserTestBuilder() {
    }
}
