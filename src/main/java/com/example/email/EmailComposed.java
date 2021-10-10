package com.example.email;

import java.util.List;
import java.util.Objects;

public final class EmailComposed {

    private final List<String> targets;
    private final String subject;
    private final String body;

    public EmailComposed(List<String> targets, String subject, String body) {
        this.targets = targets;
        this.subject = subject;
        this.body = body;
    }

    public List<String> targets() {
        return targets;
    }

    public String subject() {
        return subject;
    }

    public String body() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EmailComposed) obj;
        return Objects.equals(this.targets, that.targets) &&
            Objects.equals(this.subject, that.subject) &&
            Objects.equals(this.body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targets, subject, body);
    }

    @Override
    public String toString() {
        return "EmailComposed[" +
            "targets=" + targets + ", " +
            "subject=" + subject + ", " +
            "body=" + body + ']';
    }
}
