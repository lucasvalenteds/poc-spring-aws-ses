package com.example.user;

import com.example.email.EmailProperties;

import java.util.Objects;

public final class UserCreatedProperties implements EmailProperties {

    private final User user;

    public UserCreatedProperties(User user) {
        this.user = user;
    }

    public User user() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserCreatedProperties) obj;
        return Objects.equals(this.user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return "UserCreatedProperties[" +
            "user=" + user + ']';
    }

}
