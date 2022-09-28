package io.devnindo.service.realtime;

import io.devnindo.datatype.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.core.http.WebSocket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;

@Singleton
public class RltManager
{
    Vertx vertx;

    @Inject
    public RltManager(Vertx vertx$)
    {
        vertx = vertx$;
    }

    public void subscribe(RltTopic topic, WebSocket socket)
    {
        MessageConsumer<JsonObject> msgConsumer = vertx.eventBus().consumer(topic.getTopicId());
        msgConsumer.handler(h -> {
            socket.writeTextMessage(h.body().encode());

        });
        if(topic.maxAge != null)
            vertx.setTimer(topic.maxAge, h -> {
                msgConsumer.rxUnregister().andThen(socket.close()).subscribe();
            });

    }

}
