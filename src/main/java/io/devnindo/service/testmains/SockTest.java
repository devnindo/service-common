package io.devnindo.service.testmains;


import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.configmodels.ConfigServer;
import io.devnindo.service.exec.auth.DefaultJwtHandler;
import io.devnindo.service.exec.auth.JWTConfig;
import io.devnindo.service.exec.auth.JwtHandlerIF;
import io.devnindo.service.realtime.RltManager;
import io.devnindo.service.realtime.RltPermission;
import io.devnindo.service.realtime.RltTopic;
import io.devnindo.service.realtime.SocketServerVerticle;
import io.devnindo.service.util.JsonArrayMessageCodec;
import io.devnindo.service.util.JsonObjectMessageCodec;
import io.vertx.core.eventbus.EventBus;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.http.HttpClient;

import java.time.Instant;

public class SockTest {


    public static RltTopic sampleTopic(){

        return new RltTopic()
                .setTopicId("game:50")
                .setUserId(123L)
                .setPermission(RltPermission.read_write)
                .setMaxAge(10);
    }

    public static JwtHandlerIF jwtHdler(){
        JWTConfig config = new JWTConfig()
                .setIssuer("devnindo")
                .setExpireInSeconds(60*60)
                .setSecret("devnindo-genjutsu-secret-rat");
        return new DefaultJwtHandler(config);
    }
    public static String signedTopic(JwtHandlerIF jwtHandler){
            return jwtHandler.generateJWT(sampleTopic().toJson());
    }

    public static Vertx initVertx(){
        Vertx vertx = Vertx.vertx();
        EventBus eventBus = vertx.eventBus().getDelegate();
        eventBus.registerDefaultCodec(JsonObject.class, new JsonObjectMessageCodec());
        eventBus.registerDefaultCodec(JsonArray.class, new JsonArrayMessageCodec());
        return vertx;
    }

    public static void testSocketClient(Vertx vertx, String topicToken){
        HttpClient client = vertx.createHttpClient();
        client.webSocket(8080, "localhost", "/rlt/"+topicToken)
                .doOnError(throwable -> throwable.printStackTrace())
                .doOnSuccess(ws -> {
                    RltTopic sampleTopic = sampleTopic();
                    vertx.setPeriodic(3000, h-> {
                        System.out.println("Vertx Periodic triggered");
                        JsonObject busMsg = new JsonObject().put("msg", "hello: "+Instant.now());
                        vertx.eventBus().publish(sampleTopic.getTopicId(), busMsg);
                        System.out.println();
                    });
                })
                .subscribe(ws -> {
                    ws.textMessageHandler(str -> {
                        System.out.println("### MSG RECEIVED: ");
                        System.out.println(str);
                    });
                });
    }

    public static void main(String... args){
        Vertx vertx = initVertx();
        JwtHandlerIF jwtHandler = jwtHdler();
        RltManager manager = new RltManager(vertx, jwtHandler);
        String topicToken = signedTopic(jwtHandler);
        ConfigServer configServer = new ConfigServer()
                .setPort(8080)
                .setSslEnabled(false);

        vertx.deployVerticle(new SocketServerVerticle(manager, configServer))
                .doOnError(throwable -> throwable.printStackTrace())
                .doOnSuccess(t ->  testSocketClient(vertx, topicToken))
                .subscribe()
        ;





    }
}
