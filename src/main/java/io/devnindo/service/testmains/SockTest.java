package io.devnindo.service.testmains;


import io.devnindo.service.configmodels.ConfigServer;
import io.devnindo.service.realtime.RltManager;
import io.devnindo.service.realtime.SocketServerVerticle;
import io.vertx.rxjava3.core.Vertx;

public class SockTest {
    public static void main(String... args){
        Vertx vertx = Vertx.vertx();
        ConfigServer configServer = new ConfigServer().setPort(8080);
        RltManager manager = new RltManager(vertx);
        vertx.deployVerticle(new SocketServerVerticle(configServer));
    }
}
