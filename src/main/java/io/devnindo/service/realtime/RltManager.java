/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.service.realtime;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.service.exec.auth.JwtHandler;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
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
    private final JwtHandler jwtHandler;
    ConcurrentMap<Long, RltRegistry> userRltMap;
    ConcurrentMap<Long, Boolean> initRegistryMap;

    private  Long periodId;
    @Inject
    public RltManager(Vertx vertx$, JwtHandler jwtHandler$)
    {
        jwtHandler = jwtHandler$;
        vertx = vertx$;
        userRltMap = new ConcurrentHashMap<>(16, 0.76f, 2);
        initRegistryMap = new ConcurrentHashMap<>(16, 0.76f, 2);
    }

     // a new topic will try closing old topic session
    // doesn't prevent parallel topic registration yet
    public void initRltSocket(RoutingContext rc){

        String token = rc.pathParam("token");
        Either<Violation, RltTopic> topicEither = jwtHandler.validateJWT(token, RltTopic.class);
        if(topicEither.isLeft())
            rc.response().setStatusCode(400).end();

        RltTopic rltTopic = topicEither.right();
        if(initRegistryMap.containsKey(rltTopic.getUserId()))
            rc.response().setStatusCode(400).end();
        else{
            // a new initialization starts here
            initRegistryMap.put(rltTopic.getUserId(), Boolean.TRUE);
        }

        // INIT_STEP_1: old registry removal
        RltRegistry registry = userRltMap.remove(rltTopic.getUserId());

        if(registry != null){
            rxCloseRegistry((short) 1003, "NEW_TOPIC_REGISTER", registry)
                    .subscribe(()-> {
                        registerTopicSocket0(rltTopic, rc).subscribe();
                    });
        }
        else{
            registerTopicSocket0(rltTopic, rc).subscribe();
        }




    }
    private Single<ServerWebSocket> registerTopicSocket0(RltTopic topic, RoutingContext rc)
    {
        return
            rc.request()
            .toWebSocket()
            .doOnSuccess(ws -> {
                MessageConsumer<JsonObject> msgConsumer = vertx.eventBus().consumer(topic.getTopicId());
                RltRegistry rltRegistry = new RltRegistry(topic, ws, msgConsumer);
                msgConsumer.handler(h -> {
                    ws.writeTextMessage(h.body().encode());
                });
                ws.textMessageHandler(h->{
                    JsonObject js = new JsonObject(h.getBytes());
                    rltRegistry.bleep();
                    System.out.println("# SERVER RECEIVED: "+js.encode());
                });
                userRltMap.put(topic.getUserId(), rltRegistry);
                initRegistryMap.remove(topic.getUserId());

                System.out.println("# SERVER REGISTRY CREATED -> TOPIC: "+topic.getTopicId());

                if(!hasPeriodicCheckerStarted())
                    startPeriodicCheck();

        });

    }

    public Completable rxCloseRegistry(short statusCode, String reasonMsg, RltRegistry registry$)
    {

        return registry$
                .webSocket.rxClose(statusCode, reasonMsg)
                .andThen(registry$.msgConsumer.rxUnregister())
                .doOnComplete(
                        ()->System.out.println("# SERVER REGISTRY REMOVED -> TOPIC: "+registry$.topic.getTopicId())
                );
    }

    private boolean hasPeriodicCheckerStarted(){
        return periodId != null;
    }

    private void startPeriodicCheck(){
        periodId = vertx.setPeriodic(500, h->{
            System.out.println("# SERVER REGISTRY LOG: "+userRltMap.size());
           userRltMap.values().forEach(aRegistry -> {
                if(aRegistry.topic.shouldExpire()){
                    userRltMap.remove(aRegistry.topic.getUserId());
                    rxCloseRegistry((short)1000, "AGE_EXPIRED", aRegistry)
                            .subscribe();
                }
                else if(aRegistry.nonResponsive(10)){
                    userRltMap.remove(aRegistry.topic.getUserId());
                    rxCloseRegistry((short)1001, "NON_RESPONSIVE", aRegistry)
                            .subscribe();
                }
           });
        });
    }




}
