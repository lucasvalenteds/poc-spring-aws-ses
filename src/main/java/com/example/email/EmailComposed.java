package com.example.email;

import java.util.List;

public final record EmailComposed(List<String> targets, String subject, String body) {
}
