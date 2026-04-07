# ivy-tracing-agent

This example shows end-to-end tracing with the OpenTelemetry Java agent in a complex system involving multiple services, including Axon Ivy Engine.
Use this setup if you need broad instrumentation coverage across third-party libraries and frameworks.

![Overview](../overview.png)

After starting this example with `docker compose up` you can go to http://localhost to start some processes. Navigate to http://localhost:16686 to see 
and analyze the recorded traces.

![Tracing](../tracing.png)

## Jaeger Tracing

The [Jaeger tracing tool](https://www.jaegertracing.io/) collects traces from different systems and provides a UI to search and analyzes traces. 

## OpenTelemetry Agent (full coverage)

The [OpenTelemetry](https://opentelemetry.io/) Java agent instruments Java applications (for example Axon Ivy Engine, Tomcat, and many third-party
libraries) and exports telemetry data to a backend such as Jaeger.

Compared to native OTLP support, the agent-based setup is more powerful because it can automatically instrument many additional integrations and protocols.

Use this sample when you need:
- broad, automatic instrumentation across third-party tools
- richer tracing coverage beyond native Ivy HTTP stacks
- one consistent instrumentation approach across heterogeneous Java services

If you prefer a lightweight setup without additional agent libraries, see the OTLP-native sample in [ivy-tracing-otlp](../ivy-tracing-otlp/README.md).
