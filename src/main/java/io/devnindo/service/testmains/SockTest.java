package io.devnindo.service.testmains;


import io.devnindo.service.realtime.SocketServerVerticle;
import io.vertx.rxjava3.core.Vertx;

public class SockTest {
    public static void main(String... args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new SocketServerVerticle());
    }
}
