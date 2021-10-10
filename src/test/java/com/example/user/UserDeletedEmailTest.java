package com.example.user;

import com.example.ServiceConfiguration;
import com.example.ThymeleafConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("local")
@SpringJUnitConfig({ServiceConfiguration.class, ThymeleafConfiguration.class})
class UserDeletedEmailTest {

    private UserDeletedEmail userDeletedEmail;

    @BeforeEach
    public void beforeEach(ApplicationContext context) {
        var templateEngine = context.getBean(ISpringWebFluxTemplateEngine.class);
        var templatesDirectory = context.getBean(Path.class);
        userDeletedEmail = new UserDeletedEmail(templateEngine, templatesDirectory);
    }

    @Test
    void testAcceptsProperties() {
        var user = UserTestBuilder.USER;
        var properties = new UserDeletedProperties(user.getEmail(), user.getFirstname());

        var accepts = userDeletedEmail.accepts(properties);

        assertTrue(accepts, "Should accept instances of UserDeletedProperties");
    }

    @Test
    void testRejectsProperties() {
        var properties = new UserCreatedProperties(UserTestBuilder.USER);

        var accepts = userDeletedEmail.accepts(properties);

        assertFalse(accepts, "Should accept instances of UserDeletedProperties only");
    }

    @Test
    void testComposesEmail() {
        var user = UserTestBuilder.USER;
        var properties = new UserDeletedProperties(user.getEmail(), user.getFirstname());

        StepVerifier.create(userDeletedEmail.compose(properties))
            .assertNext(email -> {
                assertEquals("FooX account deleted", email.subject());
                assertThat(email.targets()).containsOnly(user.getEmail());
            })
            .verifyComplete();
    }
}