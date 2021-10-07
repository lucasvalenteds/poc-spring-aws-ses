module com.example {
    requires com.fasterxml.jackson.databind;
    requires org.reactivestreams;
    requires org.slf4j;
    requires reactor.core;
    requires reactor.netty.core;
    requires reactor.netty.http;
    requires software.amazon.awssdk.auth;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.services.ses;
    requires spring.context;
    requires spring.core;
    requires spring.web;
    requires spring.webflux;
    requires thymeleaf.spring5;
    requires thymeleaf;

    opens com.example to spring.core, spring.context, spring.beans;
    opens com.example.email to spring.core, spring.context, spring.beans;
    opens com.example.user to spring.core, spring.context, spring.beans;
}