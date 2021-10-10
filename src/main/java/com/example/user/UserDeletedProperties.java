package com.example.user;

import com.example.email.EmailProperties;

public final record UserDeletedProperties(String email, String firstname) implements EmailProperties {
}
