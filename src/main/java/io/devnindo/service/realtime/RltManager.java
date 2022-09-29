package io.devnindo.service.realtime;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.tuples.Pair;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.service.exec.auth.JwtHandlerIF;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.core.http.ServerWebSocket;
import io.vertx.rxjava3.ext.web.RoutingContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class RltManager
{
    private final Vertx vertx;
    private final JwtHandlerIF jwtHandler;
    ConcurrentMap<Long, Pair<MessageConsumer, ServerWebSocket>> userRltMap;
    @Inject
    public RltManager(Vertx vertx$, JwtHandlerIF jwtHandler$)
    {
        jwtHandler = jwtHandler$;
        vertx = vertx$;
        userRltMap = new ConcurrentHashMap<>();
    }

    public void initRltSocket(RoutingContext rc){

        String token = rc.pathParam("token");
        Either<Violation, RltTopic> topicEither = jwtHandler.validateJWT(token, RltTopic.class);
        if(topicEither.isLeft())
            rc.response().setStatusCode(400).end();

        RltTopic rltTopic = topicEither.right();
        Pair<MessageConsumer, ServerWebSocket> rltRegistry = userRltMap.get(rltTopic.getUserId());
        if(rltRegistry != null){
            closeSocketBus(rltRegistry);
        }

        rc.request().toWebSocket().subscribe(ws -> this.registerTopic(rltTopic, ws));

    }
    private void registerTopic(RltTopic topic, ServerWebSocket socket)
    {
        MessageConsumer<JsonObject> msgConsumer = vertx.eventBus().consumer(topic.getTopicId());
        Pair<MessageConsumer, ServerWebSocket> pair = Pair.of(msgConsumer, socket);
        userRltMap.put(topic.getUserId(), pair);

        msgConsumer.handler(h -> {
            socket.writeTextMessage(h.body().encode());
        });

        if(topic.maxAge != null)
            vertx.setTimer(topic.getMaxAgeMillis(), h ->  closeSocketBus(pair));



    }

    private void closeSocketBus(Pair<MessageConsumer, ServerWebSocket> pair)
    {
        pair.first.rxUnregister()
                .andThen(pair.second.close())
                .subscribe(() -> System.out.println("Socket Closed for Topic: "));
    }

}
