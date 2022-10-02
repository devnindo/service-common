package io.devnindo.service.realtime;

import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.core.http.ServerWebSocket;

public class RtlRegistry
{
    final RltTopic topic;
    final ServerWebSocket webSocket;
    final MessageConsumer msgConsumer; // vertx event bus

    public RtlRegistry(RltTopic topic, ServerWebSocket webSocket, MessageConsumer msgConsumer) {
        this.topic = topic;
        this.webSocket = webSocket;
        this.msgConsumer = msgConsumer;
    }
}
