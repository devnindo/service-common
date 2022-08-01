package io.devnindo.service.testmains;

import io.vertx.reactivex.core.Vertx;

public class SockTest {
    public static void main(String... args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new SocketServerVerticle());
    }
}
