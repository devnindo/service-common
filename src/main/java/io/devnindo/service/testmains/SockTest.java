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
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class SockTest {


    public static RltTopic sampleTopic(){

        return new RltTopic()
                .setTopicId("game:50")
                .setUserId(123L)
                .setPermission(RltPermission.read_write)
                .setExpireDatime(Instant.now().plus(10, ChronoUnit.MINUTES));
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

    public static Vertx initServerVertx(){
        Vertx vertx = Vertx.vertx();
        EventBus eventBus = vertx.eventBus().getDelegate();
        eventBus.registerDefaultCodec(JsonObject.class, new JsonObjectMessageCodec());
        eventBus.registerDefaultCodec(JsonArray.class, new JsonArrayMessageCodec());
        return vertx;
    }

    public static void testSocketClient( String topicToken){
        Vertx clientVertx = Vertx.vertx();
        HttpClient client = clientVertx.createHttpClient();
        Random random = new Random();
        client.webSocket(8080, "localhost", "/rlt/"+topicToken)
                .doOnError(throwable -> throwable.printStackTrace())
                .doOnSuccess(ws -> {
                   /* clientVertx.setPeriodic(10000, h-> {

                      JsonObject serverMsg = new JsonObject().put("msg", "hello server: "+Instant.now());
                      ws.writeTextMessage(serverMsg.encode());

                    });*/
                })
                .subscribe(ws -> {
                    ws.textMessageHandler(str -> {
                        System.out.println("# CLIENT RECEIVED: "+str);
                    });
                });
    }

    public static void main(String... args){
        Vertx vertx = initServerVertx();
        JwtHandlerIF jwtHandler = jwtHdler();
        RltManager manager = new RltManager(vertx, jwtHandler);
        String topicToken = signedTopic(jwtHandler);
        ConfigServer configServer = new ConfigServer()
                .setPort(8080)
                .setSslEnabled(false);

        vertx.deployVerticle(new SocketServerVerticle(manager, configServer))
                .doOnError(throwable -> throwable.printStackTrace())
                .doOnSuccess(t ->  testSocketClient(topicToken))
                .subscribe()
        ;





    }
}
