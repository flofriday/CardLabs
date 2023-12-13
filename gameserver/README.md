# Gameserver

The server executing the bots.

This folder also includes CardScheme, a custom interpreter for CardLabs.

## Build it yourself

You need a Java 21 JDK inatalled.

```
# GameServer
./mvnw compile exec:java
```

## Testing, Linting and Formatting

```
# Run all tests
./mvnw test

# Format the code
./mvnw ktlint:format

# Lint the code
./mvnw ktlint:check
```

## Examples

There are some CardScheme examples in the `examples` folder.