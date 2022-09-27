package io.devnindo.service.realtime;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.tuples.Pair;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.service.exec.auth.BizSessionHandler;
import io.devnindo.service.exec.auth.BizUser;
import io.vertx.ext.web.RoutingContext;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.http.WebSocket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public  class SocketManager
{
    private final ConcurrentMap<Long, Pair<WebSocket, Instant>> socketMap;
    private  final BizSessionHandler bizSessionHandler;
    private final Vertx vertx;

    @Inject
    public SocketManager(BizSessionHandler sessionHandler, Vertx vertx){
        socketMap = new ConcurrentHashMap<>();
        bizSessionHandler = sessionHandler;
    }

    protected Either<Violation, WebSocket> upgradeSocket(RoutingContext routingContext)
    {

    }

    public void publish(String topicId, JsonObject topicData){
        //
    }
    public  void consume(String topicId, JsonObject topicData){}


}
