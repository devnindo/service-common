package io.devnindo.service.realtime;


import io.devnindo.service.exec.auth.BizAuth;
import io.vertx.core.Promise;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;
import io.vertx.rxjava3.ext.web.handler.sockjs.SockJSHandler;

import javax.inject.Inject;
import java.util.Map;


public class SocketServerVerticle extends AbstractVerticle
{
    Map<String, BizAuth> permAuth;

    @Inject
    public SocketServerVerticle( ){

    }

    @Override
    public void start(Promise<Void> startPromise$) throws Exception {
        //super.start();
        try{

            Router router = mountEventBus();
            HttpServer httpServer = vertx.createHttpServer();

            httpServer
                .requestHandler(router)
                .listen(8082)
                .subscribe(server -> {
                    startPromise$.complete();
                    System.out.println("Socket JS server deployed on port : " + server.actualPort());
                });
        }catch (Throwable excp){
            excp.printStackTrace();
        }


    }

    public Router mountEventBus(){
        Router router = Router.router(vertx);
        router.route("/eventbus/*")
                .handler(CorsHandler.create("*").allowCredentials(true))

        ;

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        SockJSBridgeOptions options = new SockJSBridgeOptions();
                //.addInboundPermitted(inboundPermitted)
              //  .setPingTimeout(5000);

        router.mountSubRouter("/eventbus", sockJSHandler.bridge(options));
        return router;
    }
    public Router mountRealtimeBus(){
        Router router = Router.router(vertx);
        router.route("/realtime/*")
                .handler(CorsHandler.create("*").allowCredentials(true))

        ;

        SockJSHandlerOptions options = new SockJSHandlerOptions()
                .setRegisterWriteHandler(true)
                .setHeartbeatInterval(2000) ;

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);
        router.mountSubRouter("/realtime/:token", sockJSHandler.socketHandler(sockJSSocket -> {
            HttpServerRequest request = sockJSSocket.routingContext().request();
            String accessToken = request.getParam("token");

            sockJSSocket.closeHandler( ($) -> {
                System.out.println("Closing For invalid effort");
            });
            sockJSSocket.handler( buffer -> {

                JsonObject data = new JsonObject(buffer.getDelegate().getBytes());
                System.out.println("### received: "+data.encode());
                sockJSSocket.write(new JsonObject().put("### replying", "adios!!").encode());
            });
            // Just echo the data back
            //sockJSSocket.handler(sockJSSocket::write);

        }));
        return router;
    }
}
