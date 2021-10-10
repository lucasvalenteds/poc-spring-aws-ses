package com.example.user;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public final class Credentials {

    private final String email;
    private final String password;

    @JsonCreator
    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Credentials) obj;
        return Objects.equals(this.email, that.email) &&
            Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "Credentials[" +
            "email=" + email + ", " +
            "password=" + password + ']';
    }

}
