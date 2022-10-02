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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class RltManager
{
    private final Vertx vertx;
    private final JwtHandlerIF jwtHandler;
    ConcurrentMap<Long, RtlRegistry> userRltMap;

    private  Long periodId;
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
        RtlRegistry rltRegistry = userRltMap.get(rltTopic.getUserId());
        if(rltRegistry != null){
            rltRegistry.webSocket.close((short)1003, "NEW_TOPIC_REGISTER");
        }

        rc.request().toWebSocket().subscribe(ws -> this.registerTopic(rltTopic, ws));

    }
    private void registerTopic(RltTopic topic, ServerWebSocket socket)
    {
        MessageConsumer<JsonObject> msgConsumer = vertx.eventBus().consumer(topic.getTopicId());
        RtlRegistry rltRegistry = new RtlRegistry(topic, socket, msgConsumer);
        msgConsumer.handler(h -> {
            socket.writeTextMessage(h.body().encode());
        });
        socket.binaryMessageHandler(h->{
            h.getBytes()
        });
        socket.closeHandler(h -> {
            msgConsumer
                .rxUnregister()
                .subscribe(() -> {
                    System.out.println("SocketBus has been closed for: "+topic.getTopicId());
                    userRltMap.remove(topic.getUserId());
                });
        });



        //if(topic.maxAge != null)
       //     vertx.setTimer(topic.getMaxAgeMillis(), h ->  socket.close((short)1000, "AGE_EXPIRED"));

        userRltMap.put(topic.getUserId(), rltRegistry);
        if(!hasPeriodicCheckerStarted())
            startPeriodicCheck();

    }

    private boolean hasPeriodicCheckerStarted(){
        return periodId != null;
    }

    private void startPeriodicCheck(){
        periodId = vertx.setPeriodic(200, h->{
           userRltMap.values().forEach(registry -> {
                if(registry.topic.shouldExpire()){
                    registry.webSocket.close((short)1000, "AGE_EXPIRED");
                    return;
                }
                else if(registry.nonResponsive(10)){
                    registry.webSocket.close((short)1001, "NON_RESPONSIVE");
                }
           });
        });
    }


}
