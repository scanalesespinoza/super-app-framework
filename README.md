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
recommended service settings in Java.

## Continuous Integration

A GitHub Actions workflow builds the Quarkus example and runs the tests on every
push or pull request.

## License

Distributed under the [Apache 2.0 License](LICENSE).
