# Gameserver

The server executing the bots.

This folder also includes CardScheme, a custom interpreter for CardLabs.

## Build and run it yourself

You need a Java 21 JDK inatalled.

```
# GameServer
./mvnw compile exec:java
```

## Configuration

By default the game server connects to a RabbitMQ instance on localhost with the default user and password.
However, you can change that by setting some environment variables.

- `RMQ_HOST`:  RabbitMQ Host
- `RMQ_USER`: RabbitMQ User
- `RMQ_PASSWORD`: RabbitMQ Password
- `RMQ_VIRTUALHOST`: RabbitMQ Virtual Host

## Testing, Linting and Formatting

```
# Run all tests
./mvnw test

# Format the code
./mvnw ktlint:format

# Lint the code
./mvnw ktlint:check
```