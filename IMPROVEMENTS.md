# pillar

## Overview
Collection of coarse, company-specific (Acme) domain libraries. Builds on swissknife, adding application-level conventions and serialization schemas: logging, messaging, monitoring, HTTP API conventions, and business domain models.

## Scorecard

| Dimension | Rating | Notes |
|-----------|--------|-------|
| Build system | A | Identical to swissknife, clean version catalog |
| Code quality | A | Composition over inheritance, clean adapter patterns |
| Test coverage | A- | 107 test files across 36 modules (~3 tests/module) |
| Documentation | D | 2-line README, no domain model docs |
| Dependency freshness | A | All current, delegates to swissknife |
| Modularity | A | 36 modules, clear core→correlation→DDD progression |
| Maintainability | A- | 6,220 LOC, ~10 TODOs |

## Structure
- 36 modules: Acme business domain, Avro/JSON serialization, schema validation rules, messaging, encryption, JWT, web/API, logging, monitoring
- Heavy dependency on swissknife (74 explicit library references)
- ~6,220 lines of Kotlin

## Issues
- Sparse documentation — no domain model explanation (what is a Tenant, Customer, Actor?)
- Tight coupling to swissknife (74 deps) — changes require coordinated updates
- SNAPSHOT versioning throughout
- No schema evolution strategy documented
- ~10 TODOs

## Potential Improvements
1. Document the Acme domain model (Entity Relationship Diagram, glossary)
2. Document serialization format decisions (why Avro for some, JSON for others)
3. Add schema evolution examples and strategy
4. Move to semantic versioning
5. Consider consolidating tightly-coupled Avro/JSON modules
6. Add compatibility matrix (swissknife version → pillar version)
7. Create a BOM for pillar consumers
