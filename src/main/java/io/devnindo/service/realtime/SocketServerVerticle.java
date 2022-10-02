package io.devnindo.service.realtime;


import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.service.configmodels.ConfigServer;
import io.devnindo.service.configmodels.ParamHttp;
import io.devnindo.service.exec.action.request.BizAccessInfo;
import io.devnindo.service.exec.auth.BizSessionHandler;
import io.devnindo.service.exec.auth.JwtHandlerIF;
import io.devnindo.service.util.Values;
import io.vertx.core.Promise;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.core.http.WebSocket;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;
import io.vertx.rxjava3.ext.web.handler.sockjs.SockJSHandler;

import javax.inject.Inject;


public class SocketServerVerticle extends AbstractVerticle
{

    private final HttpServerOptions serverOps;
    private final RltManager rltManager;

    public SocketServerVerticle(RltManager rltManager$, ConfigServer serverConfig$)
    {

        rltManager = rltManager$;
        serverOps = new HttpServerOptions()
                .setPort(serverConfig$.getPort())
                .setCompressionSupported(true)
                .setSsl(serverConfig$.getSslEnabled())
                .setPemKeyCertOptions(new PemKeyCertOptions()
                        .setCertPath(serverConfig$.getSslCertLocation())
                        .setKeyPath(serverConfig$.getSslKeyLocation()));
    }

    @Override
    public void start(Promise<Void> startPromise$) throws Exception {
        //super.start();
        try{

            Router router = Router.router(vertx);
            router
                .get("/rlt/:token")
                .handler(rltManager::initRltSocket);

            HttpServer httpServer = vertx.createHttpServer(serverOps);

            httpServer
                .requestHandler(router)
                .listen(serverOps.getPort())
                .subscribe(server -> {
                    System.out.println(serverOps.getPort());
                    System.out.println("web-socket server deployed on port : " + server.actualPort());
                    startPromise$.complete();
                });
        }catch (Throwable excp){
            excp.printStackTrace();
        }


    }



    /*public Router mountEventBus(){
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
    }*/
    public Router mountRealtimeBus(){
        Router router = Router.router(vertx);

        SockJSHandlerOptions options = new SockJSHandlerOptions()
                .setRegisterWriteHandler(true)
                .setHeartbeatInterval(2000) ;
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);
        sockJSHandler.socketHandler(socket -> {
            HttpServerRequest request = socket.routingContext().request();
            String accessToken = request.getParam("token");
            if(accessToken.equals("test-socket-bus") != false) // accesstoken not valid
            {
                socket.close(401, "Unauthorized");
            }
            socket.write("ping");
        });

        router.route("/realtime/:token")
                .handler(
                    CorsHandler.create("*")
                    .allowedHeader(ParamHttp.CONTENT_TYPE)
                )
                .handler(sockJSHandler)

        ;


/*
        router.route("/:token", sockJSHandler.socketHandler(sockJSSocket -> {
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

        }));*/
        return router;
    }
}
