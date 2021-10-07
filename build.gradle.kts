import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    application
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j", "log4j-api", properties["version.log4j"].toString())
    implementation("org.apache.logging.log4j", "log4j-core", properties["version.log4j"].toString())
    implementation("org.slf4j", "slf4j-simple", properties["version.slf4j"].toString())

    implementation("org.springframework", "spring-core", properties["version.spring"].toString())
    implementation("org.springframework", "spring-context", properties["version.spring"].toString())
    implementation("org.springframework", "spring-webflux", properties["version.spring"].toString())
    testImplementation("org.springframework", "spring-test", properties["version.spring"].toString())

    implementation("io.projectreactor", "reactor-core", properties["version.reactor"].toString())
    implementation("io.projectreactor.netty", "reactor-netty-core", properties["version.netty"].toString())
    implementation("io.projectreactor.netty", "reactor-netty-http", properties["version.netty"].toString())
    testImplementation("io.projectreactor", "reactor-test", properties["version.reactor"].toString())

    implementation("com.fasterxml.jackson.core", "jackson-databind", properties["version.jackson"].toString())
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", properties["version.jackson"].toString())

    implementation("com.amazonaws", "aws-java-sdk-core", properties["version.aws.v1"].toString())
    implementation("software.amazon.awssdk", "ses", properties["version.aws.v2"].toString())

    testImplementation("org.hamcrest", "hamcrest-core", properties["version.hamcrest"].toString())
    implementation("org.thymeleaf", "thymeleaf", properties["version.thymeleaf"].toString())
    implementation("org.thymeleaf", "thymeleaf-spring5", properties["version.thymeleaf"].toString())

    testImplementation("org.junit.jupiter", "junit-jupiter", properties["version.junit"].toString())
    testImplementation("org.assertj", "assertj-core", properties["version.assertj"].toString())
    testImplementation("org.mockito", "mockito-core", properties["version.mockito"].toString())
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

configure<JavaApplication> {
    mainClass.set("com.example.Main")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
    }
    finalizedBy(tasks.withType<JacocoReport>())
}

tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
    }
}