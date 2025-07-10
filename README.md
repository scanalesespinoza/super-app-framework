# Super App Framework

Super App Framework is a project geared toward the development of **cloud-native** applications with an emphasis on resilience and ease of use.

Today there is no consistent way to build cloud‑native applications, and inefficiencies or failures are common in services that were not designed for distributed operation. This framework provides tools that simplify such developments and reduce potential points of failure.

## Key Features

- **Automatic helpers** that suggest recommended steps to create resilient services.
- **Libraries** to handle:
  - Incoming and outgoing connections.
  - Recommended timeouts.
  - Thread or process limits.
  - Health and memory management.
  - Automatic retries.
  - Transactions.
  - The global service state (available, degraded or unavailable).

## Automatic Helpers

The framework will provide recommended steps to help create resilient services.
You can see a basic mock implementation in the accompanying Quarkus example.

## Quarkus Example

A sample Quarkus project lives in the `quarkus-superapp` directory.
It injects a mock `SuperApp` instance with CDI so you can explore the
recommended service settings in Java. Source files are under
`quarkus-superapp/src/main/java`.

### Building the example

The pom generates a runnable jar for local testing. Build it with:

```bash
mvn package -f quarkus-superapp/pom.xml
```

Run the application:

```bash
java -jar quarkus-superapp/target/superapp-quarkus-1.0.0-SNAPSHOT-runner.jar
```

### Trying the endpoints

Request a user and inspect the trace:

```bash
curl -X POST -H 'Content-Type: application/json' \
  -d '{"user":"ana"}' http://localhost:8080/user
curl http://localhost:8080/trace
```

## Continuous Integration

A GitHub Actions workflow builds the Quarkus example and runs the tests on every
push or pull request.

## License

Distributed under the [Apache 2.0 License](LICENSE).
