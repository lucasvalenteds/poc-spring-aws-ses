package com.example.user;

import com.example.email.EmailException;
import com.example.email.Postman;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public final class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final Map<String, User> repository = new HashMap<>();

    private final Postman postman;

    public UserController(Postman postman) {
        this.postman = postman;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody User user) {
        if (repository.containsKey(user.getEmail())) {
            return Mono.error(new UserAlreadyExistsException(user));
        }

        return Mono.fromCallable(() -> {
                repository.put(user.getEmail(), user);
                return user;
            })
            .doOnSuccess(it -> LOGGER.info("User created successfully: {}", it.getEmail()))
            .delayUntil(userCreated -> postman.send(new UserCreatedProperties(userCreated)))
            .doOnSuccess(it -> LOGGER.info("User creation notified via e-mail"))
            .doOnError(throwable -> LOGGER.error("Could not notify user creation via e-mail", throwable));
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<User> delete(@RequestBody Credentials credentials) {
        if (!repository.containsKey(credentials.getEmail())) {
            return Mono.error(new UserNotFoundException(credentials.getEmail()));
        }

        return Mono.fromCallable(() -> repository.remove(credentials.getEmail()))
            .doOnSuccess(it -> LOGGER.info("User deleted successfully: {}", it.getEmail()))
            .delayUntil(userDeleted ->
                postman.send(new UserDeletedProperties(userDeleted.getEmail(), userDeleted.getFirstname()))
            )
            .doOnSuccess(it -> LOGGER.info("User deletion notified via e-mail"))
            .doOnError(throwable -> LOGGER.error("Could not notify user deletion via e-mail", throwable));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException() {
        // Spring Framework will set HTTP status code automatically
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleUserAlreadyExistsException() {
        // Spring Framework will set HTTP status code automatically
    }

    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleEmailException() {
        // Spring Framework will set HTTP status code automatically
    }
}