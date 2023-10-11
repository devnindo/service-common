# Service Common
Devnindo Service Common is a RPC-over-HTTP framework written following Domain Driven Deisgn(DDD) guidelines and CQRS pattern. 

This framework utilize two open source projects:
1. [Vert.x](https://vertx.io/) for HTTP server components(Routing + WebSocket + Reactive RX)
2. [Dagger](https://github.com/google/dagger) for compile time dependancy injection

 
## Installation
Add the following dependency to the dependencies section of your gradle project descriptor

```
dependencies {

    implementation('io.devnindo.core:service-common:0.9.8') { changing = true }

    implementation("com.google.dagger:dagger:2.44.2")
    annotationProcessor("com.google.dagger:dagger-compiler:2.44.2")
```

## Concepts
to be written...

## Usage
to be written...

 

## Roadmap
The following implementations are soon to be released:
1. Realtime secure topic messaging on top of websocket
2. Server Sent event for managing application wide push notifications
 

## License
For open source projects, say how it is licensed. 
