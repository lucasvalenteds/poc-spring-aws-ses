package com.example.user;

import com.example.email.Email;
import com.example.email.EmailComposed;
import com.example.email.EmailProperties;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class UserDeletedEmail implements Email {

    private static final String TEMPLATE_FILENAME = "user-deleted";
    private static final Locale TEMPLATE_LOCALE = Locale.getDefault();

    private final ISpringWebFluxTemplateEngine templateEngine;
    private final ResourceBundle resourceBundle;

    public UserDeletedEmail(ISpringWebFluxTemplateEngine templateEngine, Path templatesDirectory) {
        this.templateEngine = templateEngine;
        this.resourceBundle = ResourceBundle.getBundle(
            templatesDirectory.resolve(TEMPLATE_FILENAME).toString(),
            TEMPLATE_LOCALE
        );
    }

    @Override
    public boolean accepts(EmailProperties properties) {
        return properties instanceof UserDeletedProperties;
    }

    @Override
    public Mono<EmailComposed> compose(EmailProperties emailProperties) {
        var properties = (UserDeletedProperties) emailProperties;
        var subject = resourceBundle.getString("email.subject");

        var variables = Map.<String, Object>ofEntries(
            Map.entry("title", subject),
            Map.entry("name", properties.firstname())
        );

        return Mono
            .fromCallable(() -> templateEngine.process(TEMPLATE_FILENAME, new Context(TEMPLATE_LOCALE, variables)))
            .subscribeOn(Schedulers.boundedElastic())
            .map(body -> new EmailComposed(List.of(properties.email()), subject, body));
    }
}
