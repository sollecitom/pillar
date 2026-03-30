# Pillar - Context for AI Agents and Future-Self

## Project Purpose and Scope

Pillar is a collection of company-specific (Acme) libraries built on top of [swissknife](../swissknife). While swissknife provides general-purpose, reusable infrastructure (correlation, messaging abstractions, web API primitives, serialization frameworks, DDD building blocks), pillar adds the Acme-specific implementations: concrete JWT claim schemas, company naming conventions, serialization format definitions, schema compliance rules, and business domain models.

The relationship: **swissknife defines interfaces and abstractions; pillar provides the Acme-specific implementations and conventions.**

Pillar also depends on [acme-schema-catalogue](../acme-schema-catalogue) for shared schema definitions (Avro `.avsc` and JSON Schema `.json` files referenced by serialization modules).

## Domain Model Overview

### Tenant
Defined in swissknife (`swissknife.correlation.core.domain.tenancy.Tenant`). Represents a top-level organizational boundary. In pillar, the Acme tenant is `Tenant(id = StringId("acme.com"))`. A tenant scopes all operations and can be specified per-request via gateway headers.

### Customer
Defined in swissknife (`swissknife.correlation.core.domain.access.customer.Customer`). Represents a paying organization/company within a tenant (e.g., "Wayne Industries"). Has an `id` and an `isTest` flag. The customer ID is extracted from the JWT `orgId` claim. Customers belong to a tenant.

### Organization
Defined in pillar (`pillar.jwt.domain.Organization`). A JWT-level concept representing the organization a user belongs to, with an `id` (from JWT `orgId` claim) and a `name` (derived from the user's email domain). Maps to `Customer` when building the invocation context.

### User
Defined in pillar (`pillar.jwt.domain.User`). Represents an authenticated user extracted from JWT claims. Contains `id` (JWT subject), `organization`, `userName`, `emailAddress`, `firstName`, `lastName`, `otherNames`, and a computed `fullName`.

### Actor
Defined in swissknife (`swissknife.correlation.core.domain.access.actor.Actor`). Represents who is performing an action. The primary subtype used is `Actor.UserAccount`, which has an `id`, `locale`, `customer`, and `tenant`. Created from JWT parameters in `GatewayInvocationContextFilter`. Other actor types (service accounts, impersonation) are defined in swissknife but not yet fully supported in pillar's JWT parsing.

### Access
Defined in swissknife. Represents the access level of a request: `Access.Authenticated` (has an actor, origin, authorization, scope) or `Access.Unauthenticated` (has origin, authorization, scope, isTest flag). The web filters build the appropriate Access from JWT claims or lack thereof.

### InvocationContext
Defined in swissknife. The central correlation object that flows through every request and message. Contains `access`, `trace` (invocation ID, external IDs), `toggles` (feature flags), locale, target customer, and target tenant. Created at the gateway layer and propagated via HTTP headers and message properties.

### CompanyConventions
Defined in pillar (`pillar.acme.conventions.CompanyConventions`). A composite marker interface that bundles all convention interfaces: `MonitoringConventions`, `LoggingConventions`, `ContainerBasedTestConventions`, `MessagingConventions`, `LocaleConventions`, `HttpApiConventions`. The `Acme` object implements this interface, providing a single entry point for all conventions.

### Roles
Defined in pillar (`pillar.acme.business.domain.AcmeRoles`). The Acme-specific roles: `customer`, `expert`, `admin`. Extracted from JWT `realm_access.roles` claims, filtering to only recognized Acme roles.

## Module Organization

The project is organized into 36 modules under `modules/`:

- **acme/** - Business domain and company conventions
- **jwt/** - JWT claim parsing, domain types, and test utilities
- **web/** - HTTP API utilities (gateway filters, routing, test specs)
- **messaging/** - Event processing, topic/serde pairing, Pulsar integration
- **correlation/** - Logging context utilities for invocation correlation
- **json/** - JSON serialization (core types, DDD, correlation, pagination, web API, schema rules)
- **avro/** - Avro serialization (core types, DDD, correlation, schema rules)
- **encryption/** - Encryption metadata Avro serialization
- **protected-value/** - Protected/encrypted value types and serialization
- **http/** - HTTP API definition conventions
- **service/** - Service lifecycle logging and container-based test utilities
- **prometheus/** - Prometheus/Micrometer meter registry setup
- **open-api/** - OpenAPI specification compliance rules

## Key Patterns

### JWT and Authentication
The `AcmeJwtScheme` object defines Acme's JWT claim structure, based on Keycloak tokens. It parses/creates JWT claims following OpenID Connect conventions. The `GatewayInvocationContextFilter` intercepts HTTP requests, extracts the bearer token, verifies it via `JwtProcessor`, parses the Acme-specific claims, and builds an `InvocationContext<Access.Authenticated>`. Verified JWTs are cached (default: 500 entries, 30-minute expiry).

### Gateway Filters (HTTP Request Pipeline)
Two filter stacks are provided:
- **`GatewayHttpFilter`** - For API gateway services: catches errors, handles lens failures, adds Micrometer metrics, parses JWT from Authorization header, parses invocation context from gateway header, adds context to logging stack.
- **`StandardHttpFilter`** - For internal services behind a gateway: catches errors, handles lens failures, adds metrics, decompresses requests, parses invocation context from gateway header (no JWT parsing), adds context to logging stack.

Both use Kotlin context receivers to inject `HttpApiDefinition` and `CoreDataGenerator`.

### Routing Extensions
The `RoutingExtensions` file provides `toAuthenticated`, `toUnauthenticated`, and `toWithInvocationContext` infix functions for http4k route definitions. These extract the `InvocationContext` from the request and run the handler within the appropriate context.

### Messaging Conventions
- `AcmeMessagePropertyNames` defines Acme-specific message property names (e.g., event type property is `"event-type"`).
- `TopicAndJsonSerde` / `TopicAndSerde` bundle a topic with its serializer, implementing `MessageConverter`.
- `MessagingEventProcessor` processes message flows with forked invocation contexts, handling success/failure/no-op outcomes.
- Pulsar integration modules provide convenience functions to create consumers/producers from `TopicAndSerde` pairs.

### Serialization
Two parallel serialization stacks exist:
- **JSON** (`json/serialization/`): Uses `JsonSerde.SchemaAware<T>` with JSON Schema validation. Covers core types (Id, CurrencyAmount, FileContent, MonthAndYear), DDD events, correlation context, pagination, and web API errors.
- **Avro** (`avro/serialization/`): Uses `AvroSerde<T>` with Avro schemas. Covers the same domain concepts for message-based communication.

Both stacks have corresponding schema compliance rule modules that enforce naming conventions (lowercase-kebab for JSON fields, lowercase-underscore for Avro fields, mandatory namespace prefixes for Avro).

### Test Specifications
- `AcmeJsonSerdeTestSpecification` and `AcmeAvroSerdeTestSpecification` combine serialization round-trip testing with schema compliance checking using Acme-specific rules.
- `ErrorsEndpointHttpTestSpecification` provides reusable tests for HTTP error handling (invalid access, bad content type, malformed JSON, invalid version).

### Conventions via Context Receivers
Pillar makes heavy use of Kotlin context receivers. Convention interfaces (e.g., `MonitoringConventions`, `LoggingConventions`) act as capability markers. Functions require these as context parameters, ensuring conventions are only used where appropriate. For example, `prometheusMeterRegistry()` requires `MonitoringConventions` in scope.

## What Belongs Here vs. in Swissknife

**In pillar (company-specific):**
- Acme JWT claim structure and parsing (`AcmeJwtScheme`)
- Acme roles (`customer`, `expert`, `admin`)
- Acme-specific header names (prefixed with `acme-`)
- Company naming convention rules (field alphabets, namespace prefixes)
- Concrete serialization implementations (JSON/Avro serdes for domain types)
- Gateway filter compositions (which filters to chain and in what order)
- Message property names (`"event-type"`)
- Prometheus meter registry configuration with standard binders

**In swissknife (general-purpose):**
- Domain abstractions (`Tenant`, `Customer`, `Actor`, `InvocationContext`, `Event`)
- Serialization framework interfaces (`JsonSerde`, `AvroSerde`)
- Web API framework (`HttpApiDefinition`, `HttpHeaderNames`, `InvocationContextFilter`)
- Messaging abstractions (`MessageConnector`, `ReceivedMessage`, `MessageProducer`)
- JWT processing (`JwtProcessor`, `JWT`)
- Compliance checker framework (`ComplianceRule`, `ComplianceRuleSet`)

## Known Issues and TODOs

1. **Hardcoded `Example.tenant`**: In `GatewayInvocationContextFilter.actor()`, the tenant is hardcoded to `Example.tenant` when creating a `UserAccount`. The `Example` object itself is marked `// TODO remove`. This should be resolved so the tenant is derived dynamically (e.g., from JWT claims or configuration).

2. **Customer lookup**: In `GatewayInvocationContextFilter`, the `customerWithId()` function always returns `Customer(id = id, isTest = false)`. The TODO says `"look this up from an injected function"` -- the `isTest` flag should come from an actual customer registry.

3. **Actor extensibility**: The `actor()` function in `GatewayInvocationContextFilter` has a TODO to `"add support for service accounts, user locale, other actor types, other authentication mechanisms, etc."`. Currently only `DirectActor` with `UserAccount` is supported.

4. **Access scope**: `accessScope()` always returns an empty container stack with a TODO to `"add support for access scopes after we'll support this in JWT via custom attributes"`.

5. **Invocation context forking**: In `GatewayInfoContextParsingFilter`, there is a TODO noting `"this should be forked!"` when deserializing invocation context from the gateway header.

6. **Tracing headers OpenAPI rules**: `AcmeTracingHeadersOpenApiRules` has an empty rule set with a comment `"put some rules here"`.

## Build and Dependency Conventions

- **Build system**: Gradle with Kotlin DSL, using version catalogs (`libs.versions.toml` from swissknife).
- **Composite builds**: `settings.gradle.kts` uses `includeBuild("../swissknife")` and `includeBuild("../acme-schema-catalogue")` for local development.
- **Plugin conventions**: Custom Gradle plugins from `../gradle-plugins` (e.g., `sollecitom.dependency-update-conventions`, `sollecitom.aggregate-test-metrics-conventions`).
- **Module naming**: Modules are declared via `module("segment1", "segment2", ...)` which creates a flat Gradle project name like `segment1-segment2`.
- **Feature preview**: `TYPESAFE_PROJECT_ACCESSORS` is enabled for type-safe project dependency references.
- **Test framework**: JUnit 5 with AssertK assertions.
