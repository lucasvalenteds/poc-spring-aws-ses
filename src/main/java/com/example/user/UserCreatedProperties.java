package com.example.user;

import com.example.email.EmailProperties;

public record UserCreatedProperties(User user) implements EmailProperties {
}
