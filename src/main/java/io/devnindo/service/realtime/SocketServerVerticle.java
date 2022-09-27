package io.devnindo.service.realtime;


import io.devnindo.service.configmodels.ParamHttp;
import io.devnindo.service.exec.action.request.BizAccessInfo;
import io.devnindo.service.exec.auth.BizSessionHandler;
import io.devnindo.service.util.Values;
import io.vertx.core.Promise;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;
import io.vertx.rxjava3.ext.web.handler.sockjs.SockJSHandler;

import javax.inject.Inject;


public class SocketServerVerticle extends AbstractVerticle
{

    private final BizSessionHandler sessionHandler;
    private final SocketManager socketManager;

    public SocketServerVerticle(BizSessionHandler sessionHandler$, SocketManager socketManager$)
    {
        sessionHandler = sessionHandler$;
        socketManager = socketManager$;
    }

    @Override
    public void start(Promise<Void> startPromise$) throws Exception {
        //super.start();
        try{

            Router router = Router.router(vertx);
            router
                .route("/rlt")
                .consumes(ParamHttp.CONTENT_JSON)
                .handler(rc -> {

                    sessionHandler
                            .executeOn(initAccessInfo0(rc))
                            .flatMap(bizUser -> upgradeToSocket0(rc, bizUser))
                            .subscribe();

                });

            HttpServer httpServer = vertx.createHttpServer(router);

            httpServer
                .listen(8082)
                .subscribe(server -> {
                    System.out.println("web-socket server deployed on port : " + server.actualPort());
                    startPromise$.complete();
                });
        }catch (Throwable excp){
            excp.printStackTrace();
        }


    }

    private BizAccessInfo initAccessInfo0(RoutingContext routingCtx$)
    {


        JsonObject accessInfo = new JsonObject();
        HttpServerRequest httpReq = routingCtx$.request();

        String accessToken = httpReq.getHeader(ParamHttp.AUTHORIZATION);
        if(accessToken==null)
        {
            accessToken = Values.NOT_AVAILABLE;
        }
        String reqIp = httpReq.remoteAddress().host();
        String agentInfo = httpReq.getHeader(ParamHttp.USER_AGENT);


        return accessInfo.put($BizAccessInfo.ACCESS_TOKEN, accessToken)
                .put($BizAccessInfo.IP, reqIp)
                .put($BizAccessInfo.USER_AGENT, agentInfo)
                .toBean(BizAccessInfo.class);

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
