package com.example.email;

import com.example.user.UserCreatedProperties;
import com.example.user.UserDeletedProperties;

public sealed interface EmailProperties
    permits UserCreatedProperties, UserDeletedProperties {
}
