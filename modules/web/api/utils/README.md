# Web API Utils

HTTP filter chains and routing extensions for Acme services. Provides `GatewayHttpFilter` (for API gateways with JWT parsing), `StandardHttpFilter` (for internal services), invocation context serialization into HTTP headers, `EndpointHttpDrivingAdapter`, and routing extensions (`toAuthenticated`, `toUnauthenticated`, `toWithInvocationContext`) for type-safe access control in route handlers.
