# Gateway

What the API gateway is responsible for, and therefore what every service behind it may take for granted.
`GatewayHttpFilter` / `GatewayInvocationContextFilter` in this module (`web-api-gateway`) are an **unfinished stub** of it; the gaps are
listed at the end.

## Position

The gateway is the only entry point for external traffic. Services are never reachable directly, so they build no
security of their own around request headers: they parse the invocation context the gateway forwards
(`StandardHttpFilter` → `parseInvocationContextFromGatewayHeader`) and trust it.

That makes the gateway the single place where untrusted input becomes trusted context. Anything it forwards is
believed downstream.

## Responsibilities

**Authenticate the caller.**
- Verify bearer JWTs: signature, an explicit algorithm allow-list, `exp` and `nbf` (with bounded clock skew), the issuer
  configured for the request's domain, and the audience of this API.
- Cache verified tokens keyed by issuer and token, never by token alone, and never beyond the token's own expiry.
- Authenticate trusted machine clients of event endpoints (e.g. Stripe webhooks) by their own scheme (signature
  verification or client credentials). Once authenticated, such clients are trusted: services publish the event metadata
  they send as-is.
- Reject failures with `401` and a fixed message. Never echo token contents or exception messages.

**Build the invocation context.**
- Actor, roles and authentication details from the verified token.
- Tenant and customer from a lookup, not constants; `isTest` from the customer record.
- Target customer and target tenant, is-test, toggles, locale: set by the gateway. Values that originate from the caller
  are honoured only when the caller is entitled to them (which roles may impersonate a customer, flip is-test or set
  toggles is gateway policy).
- Trace: a new invocation id per request, plus the external invocation and action ids.
- Origin: client IP and client info.
- Forward it in the invocation-context header as the base64url encoding (no padding) of its UTF-8 JSON
  (`InvocationContext.toHeaderValue()`), so proxies can't split or merge it on the commas in the JSON.

**Sanitise what it forwards.**
- Strip `Authorization` before forwarding.
- Strip every client-supplied header that the gateway is responsible for setting (the invocation-context header and the
  gateway header set) before adding its own. A duplicated invocation-context header downstream is an error, never
  merged.

**Coarse authorisation.**
- Decided by an authorisation query engine (Open Policy Agent or similar) that checks the caller's roles and attributes
  against permissions defined as policy, rather than by checks written into the gateway's code.
- Route-level checks from roles alone, e.g. which roles may call which endpoints, and which machine clients may post to
  which event endpoints. Event endpoints must only be reachable by the machine clients allowed to post to them: services
  don't check who sends an event, they trust it.
- Data-dependent authorisation (does this user own account A? does plan P belong to this customer?) is not the
  gateway's job: it needs domain data, so it lives in the service.

**Protect services.**
- Enforce request body size limits, including after decompression.
- Answer `400` malformed, `401` unauthenticated, `403` unauthorised, without internal details.
- Log failures by method and path only, never headers or bodies.

## Gaps in the current stub

| Responsibility | Current state |
|---|---|
| JWT audience | skipped (`setSkipDefaultAudienceValidation`); `aud` as an array is rejected |
| Issuer per domain | `request.uri.authority` is empty on Jetty, so every request uses `issuerForDomain("")` |
| Verified-token cache | keyed by token only |
| Tenant / customer | hard-coded `Example.tenant`; `Customer(isTest = false)` for every id |
| Caller-supplied gateway headers | target customer/tenant, is-test and toggles are taken from the request without an entitlement check |
| Error responses | every failure becomes `400` with the raw exception message |
| Claims | `acr` other than `0`/`1`, missing `allowed-origins` or `session_state` fail the request |
| Organisation | derived from the email domain, which is wrong for subdomains and differently-cased domains |
| Machine clients of event endpoints | no authentication scheme |
| Route-level authorisation | none |
| Body size limits | none |
| Client-supplied invocation-context header | not stripped: with the gateway's own it makes two, which the service rejects with `400` |
| Error logging | validation failures are logged with the full request URI, query included |
