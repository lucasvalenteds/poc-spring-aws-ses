package com.example.user;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public final class User {

    private final String email;
    private final String password;
    private final String firstname;
    private final String lastname;

    @JsonCreator
    public User(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return Objects.equals(this.email, that.email) &&
            Objects.equals(this.password, that.password) &&
            Objects.equals(this.firstname, that.firstname) &&
            Objects.equals(this.lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, firstname, lastname);
    }

    @Override
    public String toString() {
        return "User[" +
            "email=" + email + ", " +
            "password=" + password + ", " +
            "firstname=" + firstname + ", " +
            "lastname=" + lastname + ']';
    }

}
