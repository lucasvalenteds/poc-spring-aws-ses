package com.example.user;

import com.example.email.Email;
import com.example.email.EmailComposed;
import com.example.email.EmailProperties;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class UserCreatedEmail implements Email {

    private static final String TEMPLATE_FILENAME = "user-created";
    private static final Locale TEMPLATE_LOCALE = Locale.forLanguageTag("pt");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
        DateTimeFormatter.ofPattern("LLL dd, yyyy");

    private final ISpringWebFluxTemplateEngine templateEngine;
    private final ResourceBundle resourceBundle;

    public UserCreatedEmail(ISpringWebFluxTemplateEngine templateEngine, Path templatesDirectory) {
        this.templateEngine = templateEngine;
        this.resourceBundle = ResourceBundle.getBundle(
            templatesDirectory.resolve(TEMPLATE_FILENAME).toString(),
            TEMPLATE_LOCALE
        );
    }

    @Override
    public boolean accepts(EmailProperties properties) {
        return properties instanceof UserCreatedProperties;
    }

    @Override
    public Mono<EmailComposed> compose(EmailProperties emailProperties) {
        var properties = (UserCreatedProperties) emailProperties;
        var subject = resourceBundle.getString("email.subject");
        var user = properties.user();

        var variables = Map.<String, Object>ofEntries(
            Map.entry("title", subject),
            Map.entry("name", user.getFirstname() + " " + user.getLastname()),
            Map.entry("timestamp", LocalDate.now().format(TIMESTAMP_FORMATTER))
        );

        return Mono
            .fromCallable(() -> templateEngine.process(TEMPLATE_FILENAME, new Context(TEMPLATE_LOCALE, variables)))
            .subscribeOn(Schedulers.boundedElastic())
            .map(body -> new EmailComposed(List.of(user.getEmail()), subject, body));
    }
}