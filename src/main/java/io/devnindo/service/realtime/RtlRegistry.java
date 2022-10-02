package io.devnindo.service.realtime;

import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.core.http.ServerWebSocket;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RtlRegistry
{
    final RltTopic topic;
    final ServerWebSocket webSocket;
    final MessageConsumer msgConsumer; // vertx event bus

     Instant lastPulse;

    public RtlRegistry(RltTopic topic, ServerWebSocket webSocket, MessageConsumer msgConsumer) {
        this.topic = topic;
        this.webSocket = webSocket;
        this.msgConsumer = msgConsumer;
        lastPulse = Instant.now();
    }

    public void bleep(){
        lastPulse = Instant.now();
    }

    public boolean nonResponsive(long timeSpan){
        return lastPulse
                .plus(timeSpan, ChronoUnit.SECONDS)
                .isBefore(Instant.now() );
    }
}
