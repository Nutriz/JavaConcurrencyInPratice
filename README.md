# WIP

# Notes: Java Concurrency In Practice

It is not about thread, but about accessible states

## Types of multi-threading issues

- Race condition

> Stateless objets are always thread-safe



Single java statements can be multiples operations for the JVM.

To avoid multi-threading issues, we have some tools:

- `synchronized` keyword to allow only one thread at a time access method or code block
- Atomic objects in `java.util.concurrent.atomic` package

## What is thread safety



> Thread-safe classes encapsulate any needed synchronisation so that clients need not provide their own

### synchronized keyword

Pro:

- Protects from Race condition

Drawbacks:

- Slower
- No JVM optimization
- Can create bottleneck