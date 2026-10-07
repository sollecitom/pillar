# Messaging Test Utils

Test utilities for event processing:

- `processAndWaitUntilAllAcked(handler)` creates an event processor for a list of spy messages, starts it, and waits until every message is acknowledged. If the processor hits a fatal failure (e.g. an undecodable message), the call fails with that error instead of halting the process.
- `asReceivedEventSpy()` turns an event into a received message spy carrying its event-type property.
- `UndecodableMessageSpy.withType(...)` / `.withoutType()` are received messages whose payload can't be decoded.
