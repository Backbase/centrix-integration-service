# centrix-integration-service

This service facilitates single-sign-on with Centrix and returns a `redirectUrl` to navigate the user to the Centrix
website where they can perform positive pay functions.

Additional Centrix-specific documentation can be found in the `documentation` directory.

## Getting Started
* [Extend and build](https://community.backbase.com/documentation/ServiceSDK/latest/extend_and_build)

## Dependencies

Requires a running Eureka registry, by default on port 8080.

## Configuration

Service configuration is under `src/main/resources/application.yaml`.

OpenAPI specification is under `src/main/resources/spec/client-api-v1.yaml`.

The following properties should be provided by your customer:
- baseUrl
- systemIdCode
- encryptionKey
- initializationVector

## Running

To run the service in development mode, use:
- `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"`

## Authorization

Requests to this service are authorized with a Backbase Internal JWT, therefore you must access this service via the 
Backbase Gateway after authenticating with the authentication service.

For local development, an internal JWT can be created from http://jwt.io, entering `JWTSecretKeyDontUseInProduction!` 
as the secret in the signature to generate a valid signed JWT.

This service is further protected by Access Control and requires the following entitlement:

`Payments - Manage Positive Pay - view`

See [CentrixController.java](src/main/java/com/backbase/accelerators/centrix/controller/CentrixController.java)

## Community Documentation

Add links to documentation including setup, config, etc.

## Jira Project

Add link to Jira project.

## Confluence Links
Links to relevant confluence pages (design etc).

