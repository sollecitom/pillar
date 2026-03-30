# JSON Serialization Core

JSON serdes for core domain types: `Id`, `CurrencyAmount`, `FileContent` (inline and remote variants), `MonthAndYear`, and `Unit`. Each serde implements `JsonSerde.SchemaAware` backed by Acme JSON Schema definitions.
