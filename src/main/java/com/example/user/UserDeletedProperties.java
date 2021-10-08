package com.example.user;

import com.example.email.EmailProperties;

public record UserDeletedProperties(String email, String firstname) implements EmailProperties {
}
