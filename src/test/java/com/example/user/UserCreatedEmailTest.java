package com.example.user;

import com.example.ThymeleafConfiguration;
import com.example.testing.TestingConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import reactor.test.StepVerifier;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig({TestingConfiguration.class, ThymeleafConfiguration.class})
class UserCreatedEmailTest {

    private UserCreatedEmail userCreatedEmail;

    @BeforeEach
    public void beforeEach(ApplicationContext context) {
        var templateEngine = context.getBean(ISpringWebFluxTemplateEngine.class);
        var templatesDirectory = context.getBean(Path.class);
        this.userCreatedEmail = new UserCreatedEmail(templateEngine, templatesDirectory);
    }

    @Test
    void testAcceptsProperties() {
        var properties = new UserCreatedProperties(UserTestBuilder.USER);

        var accepts = userCreatedEmail.accepts(properties);

        assertTrue(accepts, "Should accept instances of UserCreatedProperties");
    }

    @Test
    void testRejectsProperties() {
        var user = UserTestBuilder.USER;
        var properties = new UserDeletedProperties(user.email(), user.firstname());

        var accepts = userCreatedEmail.accepts(properties);

        assertFalse(accepts, "Should accept instances of UserCreatedProperties only");
    }

    @Test
    void testComposesEmail() {
        var user = UserTestBuilder.USER;
        var properties = new UserCreatedProperties(user);

        StepVerifier.create(userCreatedEmail.compose(properties))
            .assertNext(email -> {
                assertEquals("Welcome to FooX!", email.subject());
                assertThat(email.body()).contains("<title>" + email.subject() + "</title>");
                assertThat(email.body()).contains("<p>Hey John Smith,</p>");
                assertThat(email.targets()).containsOnly(user.email());
            })
            .verifyComplete();
    }
}