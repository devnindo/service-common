package io.devnindo.service.testmains;


import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.configmodels.ConfigServer;
import io.devnindo.service.exec.auth.DefaultJwtHandler;
import io.devnindo.service.exec.auth.JWTConfig;
import io.devnindo.service.exec.auth.JwtHandlerIF;
import io.devnindo.service.realtime.*;
import io.devnindo.service.util.JsonArrayMessageCodec;
import io.devnindo.service.util.JsonObjectMessageCodec;
import io.vertx.core.eventbus.EventBus;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.http.HttpClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SockTest {

    static Random random = new Random();
    static JwtHandlerIF jwtHandler = jwtHdler();
    public static List<RltTopic> sampleTopicList(int sampleSize){

        List<RltTopic> topicList = new ArrayList<>();

        for(int idx=1; idx <= sampleSize; idx++)
        {
            RltTopic topic = new RltTopic()
                    .setTopicId("game:"+idx)
                    .setUserId(123L)
                    .setPermission(RltPermission.read_write)
                    .setExpireDatime(Instant.now().plus(10, ChronoUnit.MINUTES));

            topicList.add(topic);

        }
        return topicList;

    }

    public static JwtHandlerIF jwtHdler(){
        JWTConfig config = new JWTConfig()
                .setIssuer("devnindo")
                .setExpireInSeconds(60*60)
                .setSecret("devnindo-genjutsu-secret-rat");
        return new DefaultJwtHandler(config);
    }


    private static void startServerTest(Vertx serverVertx, List<RltTopic> topicList){
        serverVertx.setPeriodic(2000, h-> {
            RltTopic topic = topicList.get(random.nextInt(topicList.size()));
            JsonObject busMsg = new JsonObject().put("msg", topic.getTopicId()+" "+ Instant.now());
            serverVertx.eventBus().publish(topic.getTopicId(), busMsg);
        });
    }
    public static Vertx initServerVertx(){
        Vertx vertx = Vertx.vertx();
        EventBus eventBus = vertx.eventBus().getDelegate();
        eventBus.registerDefaultCodec(JsonObject.class, new JsonObjectMessageCodec());
        eventBus.registerDefaultCodec(JsonArray.class, new JsonArrayMessageCodec());
        return vertx;
    }

    private static void startClientListening(HttpClient client, RltTopic topic){
        String topicToken = jwtHandler.generateJWT(topic.toJson());
        client.webSocket(8080, "localhost", "/rlt/"+topicToken)
                .doOnError(throwable -> throwable.printStackTrace())
                .doOnSuccess(ws -> {
                    ws.closeHandler(h-> System.out.printf("# CLIENT SOCKET CLOSED TOPIC: %s REASON %s\n", topic.getTopicId(), ws.closeReason()));
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
    public static void testSocketClient(  List<RltTopic> topicList){
        Vertx clientVertx = Vertx.vertx();
        RltTopic topic1 = topicList.get(0);
        RltTopic topic2 = topicList.get(1);
        RltTopic topic3 = topicList.get(2);

        HttpClient client = clientVertx.createHttpClient();
        startClientListening(client, topic1);
        startClientListening(client, topic2);
        clientVertx.setTimer(10000, h -> {
            startClientListening(client, topic3);
        });

    }
    // TEST 1: two client with same user id tries to connect almost same time
    // TEST 2: a topic expire happens

    public static void main(String... args){
        Vertx vertx = initServerVertx();
        List<RltTopic> topicList = sampleTopicList(3);
        RltManager manager = new RltManager(vertx, jwtHandler);

        ConfigServer configServer = new ConfigServer()
                .setPort(8080)
                .setSslEnabled(false);

        vertx.deployVerticle(new SocketServerVerticle(manager, configServer))
                .doOnError(throwable -> throwable.printStackTrace())
                .doOnSuccess(t ->  testSocketClient( topicList))
                .subscribe(t-> startServerTest(vertx, topicList))
        ;





    }
}
