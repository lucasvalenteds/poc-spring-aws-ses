package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    Path templates(Environment environment) {
        return Path.of(environment.getRequiredProperty("templates.path", String.class));
    }

    @Bean
    ITemplateResolver templateResolver(Path templatesDirectory) {
        var templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix(templatesDirectory.toString() + File.separator);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        return templateResolver;
    }

    @Bean
    ISpringWebFluxTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        var templateEngine = new SpringWebFluxTemplateEngine();

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }
}
