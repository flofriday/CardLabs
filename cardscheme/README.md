# Cardscheme

The scheme interpreter for CardLabs.

## Build it yourself

You need a Java 21 JDK inatalled.

```
# REPL
./mvnw compile exec:java

# Execute a file
./mvnw compile exec:java  -Dexec.arguments=examples/fib.scm
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

There are some examples in the `examples` folder.