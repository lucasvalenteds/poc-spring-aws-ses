# POC: Spring AWS SES

It demonstrates how to process e-mail templates using Thymeleaf and send them via SES.

The goal is to develop a service that sends e-mails when a user is created or deleted. The e-mails should be written in HTML and its content may vary according to business logic to contain information such as user name and current date. The e-mails should be delivered via SMTP from a verified e-mail address and we should be able to implement new types of e-mails when needed without removing or changing templates already implemented. The e-mail provider should be replaceable in the same way as well.

The web service configuration should be informed via environment variables and fallback to properties file. When running for development or testing the e-mails should be sent by a Localstack instance running inside a Docker container. The container should be provisioned automatically before starting integration tests and automatically destroyed when finished.

## Software Design

There are three main participants: `Email`, `EmailProperties` and `Postman`.

`Email` implementations are responsible to provide the e-mail content, which is one e-mail template processed and represented as a text. We may have as many implementations as we want.

`EmailProperties` implementations represent the user information required to process a given e-mail template. We may have exactly one implementation per `Email` implementation.

`Postman` implementations are responsible for sending e-mails using a given SDK such as AWS SDK for SES, Jakarta Mail or Spring Email. We may have as many implementations as we want as long as they follow the `Postman` contract strictly. No changes in client code (Controllers, Services) should be made to new implementations to work properly.

Other participants such as Service classes and REST API Controllers should depend on `Postman` contract instead of its implementations and are responsible to provide the `EmailProperties` implementation based on the type of e-mail they want to send.

## How to run

### Software

| Description | Commands |
| :--- | :--- |
| Run tests | `./gradlew test` |
| Run application (AWS) | `./gradlew run --args='dev'` |
| Run application (Localstack) | `./gradlew run --args='local'` |

> Running targeting AWS requires [AWS CLI environment variables](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html) to be defined.

### Infrastructure

| Description | Command |
| :--- | :--- |
| Provision SES instance in Localstack | `make provision-localstack` |
| Destroy SES instance in Localstack | `make destroy-localstack` |
| Show Localstack logs | `make show-localstack-logs` |

### Manual

| Description | Command |
| :--- | :--- |
| Create user | `make create-user` |
| Delete user | `make delete-user` |

## Preview

### Creating a user

```
$ make create-user
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /users/create HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.79.1
> Accept: application/json
> Content-Type: application/json
> Content-Length: 90
> 
} [90 bytes data]
* Mark bundle as not supporting multiuse
< HTTP/1.1 201 Created
< Content-Type: application/json
< Content-Length: 89
< 
{ [89 bytes data]
* Connection #0 to host localhost left intact
{
  "email": "john@foocompany.com",
  "password": "123456",
  "firstname": "John",
  "lastname": "Smith"
}
```

```
infrastructure-localstack-1  | 2021-10-10 23:18:18,349:API: 127.0.0.1 - - [10/Oct/2021 23:18:18] "POST / HTTP/1.1" 200 -
infrastructure-localstack-1  | 2021-10-10T23:18:50:DEBUG:localstack.services.ses.ses_starter: Raw email
infrastructure-localstack-1  | From: hey@foocompany.com
infrastructure-localstack-1  | To: {'ToAddresses': ['john@foocompany.com'], 'CcAddresses': [], 'BccAddresses': []}
infrastructure-localstack-1  | Subject: Welcome to FooX!
infrastructure-localstack-1  | Body:
infrastructure-localstack-1  | <!DOCTYPE html>
infrastructure-localstack-1  | <html>
infrastructure-localstack-1  | <head>
infrastructure-localstack-1  |     <title>Welcome to FooX!</title>
infrastructure-localstack-1  |     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
infrastructure-localstack-1  | </head>
infrastructure-localstack-1  | <body>
infrastructure-localstack-1  | <p>Hey John Smith,</p>
infrastructure-localstack-1  | <p>
infrastructure-localstack-1  |     You have been successfully created your <b>FooX</b> account. Thank you for join
infrastructure-localstack-1  |     us in this amazing journey to improve the world.
infrastructure-localstack-1  | </p>
infrastructure-localstack-1  | <p>
infrastructure-localstack-1  |     Best regards, <br/>&emsp;<em>Foo Company Team</em>
infrastructure-localstack-1  | </p>
infrastructure-localstack-1  | <p>
infrastructure-localstack-1  |     <span>Oct 10, 2021</span>
infrastructure-localstack-1  | <p>
infrastructure-localstack-1  | </body>
infrastructure-localstack-1  | </html>
infrastructure-localstack-1  | 2021-10-10 23:18:50,124:API: 127.0.0.1 - - [10/Oct/2021 23:18:50] "POST / HTTP/1.1" 200 -
```

### Deleting a user

```
$ make delete-user
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /users/delete HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.79.1
> Accept: application/json
> Content-Type: application/json
> Content-Length: 51
> 
} [51 bytes data]
* Mark bundle as not supporting multiuse
< HTTP/1.1 202 Accepted
< Content-Type: application/json
< Content-Length: 89
< 
{ [89 bytes data]
* Connection #0 to host localhost left intact
{
  "email": "john@foocompany.com",
  "password": "123456",
  "firstname": "John",
  "lastname": "Smith"
}
```

```
infrastructure-localstack-1  | 2021-10-10 23:18:50,124:API: 127.0.0.1 - - [10/Oct/2021 23:18:50] "POST / HTTP/1.1" 200 -
infrastructure-localstack-1  | 2021-10-10T23:21:59:DEBUG:localstack.services.ses.ses_starter: Raw email
infrastructure-localstack-1  | From: hey@foocompany.com
infrastructure-localstack-1  | To: {'ToAddresses': ['john@foocompany.com'], 'CcAddresses': [], 'BccAddresses': []}
infrastructure-localstack-1  | Subject: FooX account deleted
infrastructure-localstack-1  | Body:
infrastructure-localstack-1  | <!DOCTYPE html>
infrastructure-localstack-1  | <html>
infrastructure-localstack-1  | <head>
infrastructure-localstack-1  |     <title>FooX account deleted</title>
infrastructure-localstack-1  |     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
infrastructure-localstack-1  | </head>
infrastructure-localstack-1  | <body>
infrastructure-localstack-1  | <p>Hey John,</p>
infrastructure-localstack-1  | <p>
infrastructure-localstack-1  |     We're sorry to see you go. Thank you for being with us and feel
infrastructure-localstack-1  |     free to come back whenever you want.
infrastructure-localstack-1  | </p>
infrastructure-localstack-1  | <p>
infrastructure-localstack-1  |     See you soon, <br/>&emsp;<em>Foo Company Team</em>
infrastructure-localstack-1  | </p>
infrastructure-localstack-1  | </body>
infrastructure-localstack-1  | </html>
infrastructure-localstack-1  | 2021-10-10 23:21:59,691:API: 127.0.0.1 - - [10/Oct/2021 23:21:59] "POST / HTTP/1.1" 200 -
```
