# JSON Serialization Correlation Core

JSON serdes for the full invocation context model: `InvocationContext`, `Access` (authenticated/unauthenticated), `Actor` (direct, impersonating, on-behalf, service/user accounts), `Authentication` (credentials-based, federated, stateless), `AuthorizationPrincipal`, `Roles`, `Origin`, `ClientInfo`, `Session`, `AccessScope`, `Customer`, `Tenant`, `Toggles`, and `Trace`. This is the largest serialization module, covering the complete correlation context graph.
