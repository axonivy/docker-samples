# ivy-tracing-otlp

This example shows lightweight tracing with the built-in OTLP support of Axon Ivy Engine in a complex system involving multiple services.
Use this setup if you want tracing without adding a Java agent or extra instrumentation libraries.

![Overview](../overview.png)

After starting this example with `docker compose up` you can go to http://localhost to start some processes. Navigate to http://localhost:16686 to see 
and analyze the recorded traces.

![Tracing](../tracing.png)

## Jaeger Tracing

The [Jaeger tracing tool](https://www.jaegertracing.io/) collects traces from different systems and provides a UI to search and analyzes traces. 

## Native OTLP (lightweight, built-in)

Axon Ivy Engine can export traces natively via OTLP by using environment variables or Java system properties.
No additional Java agent or third-party instrumentation library is required.

Use this sample when you need:
- a minimal setup
- built-in tracing with low operational overhead
- configuration-only enablement via environment or system property variables

Limitations of native OTLP support:
- trace-context propagation is HTTP-only and follows [W3C Trace Context](https://www.w3.org/TR/trace-context/)
- only supported in combination with native Ivy HTTP stacks: Tomcat, CXF (SOAP), and Jersey (REST)
- narrower instrumentation coverage than the agent-based setup

If you need broader third-party instrumentation, use the agent-based sample in [ivy-tracing-agent](../ivy-tracing-agent/README.md).
