package com.example.user;

import com.example.email.EmailProperties;

import java.util.Objects;

public final class UserDeletedProperties implements EmailProperties {

    private final String email;
    private final String firstname;

    public UserDeletedProperties(String email, String firstname) {
        this.email = email;
        this.firstname = firstname;
    }

    public String email() {
        return email;
    }

    public String firstname() {
        return firstname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDeletedProperties) obj;
        return Objects.equals(this.email, that.email) &&
            Objects.equals(this.firstname, that.firstname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstname);
    }

    @Override
    public String toString() {
        return "UserDeletedProperties[" +
            "email=" + email + ", " +
            "firstname=" + firstname + ']';
    }

}
