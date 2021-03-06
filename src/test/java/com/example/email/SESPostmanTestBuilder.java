package com.example.email;

import org.springframework.http.HttpStatus;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.VerifyEmailAddressResponse;

import java.util.UUID;

public final class SESPostmanTestBuilder {

    public static final String SOURCE = "testing@foocompany.com";

    private SESPostmanTestBuilder() {
    }

    public static SendEmailResponse createSesSuccessfulSendEmailResponse() {
        return SESPostmanTestBuilder.createSesSendEmailResponse(HttpStatus.OK);
    }

    public static SendEmailResponse createSesFailureSendEmailResponse() {
        return SESPostmanTestBuilder.createSesSendEmailResponse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static SendEmailResponse createSesSendEmailResponse(HttpStatus httpStatus) {
        return (SendEmailResponse) SendEmailResponse.builder()
            .messageId(UUID.randomUUID().toString())
            .sdkHttpResponse(
                SdkHttpResponse.builder()
                    .statusCode(httpStatus.value())
                    .build()
            )
            .build();
    }

    public static VerifyEmailAddressResponse createSesSuccessfulVerifyEmailAddressResponse() {
        return SESPostmanTestBuilder.createSesVerifyEmailAddressResponse(HttpStatus.OK);
    }

    public static VerifyEmailAddressResponse createSesFailureVerifyEmailAddressResponse() {
        return SESPostmanTestBuilder.createSesVerifyEmailAddressResponse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static VerifyEmailAddressResponse createSesVerifyEmailAddressResponse(HttpStatus httpStatus) {
        return (VerifyEmailAddressResponse) VerifyEmailAddressResponse.builder()
            .sdkHttpResponse(
                SdkHttpResponse.builder()
                    .statusCode(httpStatus.value())
                    .build()
            )
            .build();
    }
}
