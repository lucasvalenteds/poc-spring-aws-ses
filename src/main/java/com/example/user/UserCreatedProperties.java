package com.example.user;

import com.example.email.EmailProperties;

public final record UserCreatedProperties(User user) implements EmailProperties {
}
