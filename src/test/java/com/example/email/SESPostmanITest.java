package com.example.email;

import com.example.SESConfiguration;
import com.example.ServiceConfiguration;
import com.example.ThymeleafConfiguration;
import com.example.user.UserConfiguration;
import com.example.user.UserTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(ServiceConfiguration.LOCAL)
@SpringJUnitConfig({
    ServiceConfiguration.class,
    SESConfiguration.class,
    ThymeleafConfiguration.class,
    EmailConfiguration.class,
    UserConfiguration.class
})
@Testcontainers
class SESPostmanITest {

    @Container
    private static final LocalStackContainer CONTAINER =
        new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.15"))
            .withServices(LocalStackContainer.Service.SES);

    @DynamicPropertySource
    private static void setApplicationProperties(DynamicPropertyRegistry registry) {
        registry.add("aws.accessKey", CONTAINER::getAccessKey);
        registry.add("aws.secretKey", CONTAINER::getSecretKey);
        registry.add("aws.region", CONTAINER::getRegion);
        registry.add("aws.url", () -> CONTAINER.getEndpointOverride(LocalStackContainer.Service.S3));
        registry.add("aws.ses.source", () -> "it.testing@foocompany.com");
    }

    private Postman postman;

    @BeforeEach
    public void beforeEach(ApplicationContext context) {
        this.postman = context.getBean(Postman.class);
    }

    static Stream<Arguments> emailProperties() {
        return Stream.of(
            Arguments.of(UserTestBuilder.USER_CREATED_PROPERTIES),
            Arguments.of(UserTestBuilder.USER_DELETED_PROPERTIES)
        );
    }

    @ParameterizedTest
    @MethodSource("emailProperties")
    void testSendingEmail(EmailProperties emailProperties) {
        var send = postman.send(emailProperties);

        StepVerifier.create(send)
            .verifyComplete();
    }
}
