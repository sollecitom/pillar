# Messaging Domain

Event processing infrastructure for message-based systems. Provides `MessagingEventProcessor` (an `EventProcessor` backed by message flows) and `processWithForkedContext` which forks the invocation context for each received message and adds it to the logging stack.
