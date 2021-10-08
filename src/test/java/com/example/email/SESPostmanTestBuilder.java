package com.example.email;

import org.springframework.http.HttpStatus;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import java.util.UUID;

public final class SESPostmanTestBuilder {

    public static final String SOURCE = "testing@foocompany.com";

    private SESPostmanTestBuilder() {
    }

    public static SendEmailResponse createSesSuccessfulResponse() {
        return SESPostmanTestBuilder.createSesResponse(HttpStatus.OK);
    }

    public static SendEmailResponse createSesFailureResponse() {
        return SESPostmanTestBuilder.createSesResponse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static SendEmailResponse createSesResponse(HttpStatus httpStatus) {
        return (SendEmailResponse) SendEmailResponse.builder()
            .messageId(UUID.randomUUID().toString())
            .sdkHttpResponse(
                SdkHttpResponse.builder()
                    .statusCode(httpStatus.value())
                    .build()
            )
            .build();
    }
}
