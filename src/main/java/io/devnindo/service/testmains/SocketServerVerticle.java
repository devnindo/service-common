package io.devnindo.service.testmains;


import io.devnindo.service.exec.auth.BizAuth;
import io.vertx.core.Promise;
import io.devnindo.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.CorsHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

import javax.inject.Inject;
import java.util.Map;


public class SocketServerVerticle extends AbstractVerticle
{
    Map<String, BizAuth> permAuth;

    @Inject
    SocketServerVerticle( ){

    }

    @Override
    public void start(Promise<Void> startPromise$) throws Exception {
        //super.start();
        try{

            Router router = mountRealtimeBus();
            HttpServer server = vertx.createHttpServer();

            server
                    .requestHandler(router)
                    .listen(8082, (async) -> {
                        startPromise$.complete();
                        System.out.println("Socket JS server deployed on port : " + async.result().actualPort());
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
      /*  SockJSHandlerOptions options = new SockJSHandlerOptions()
                .setRegisterWriteHandler(true)
                .setSessionTimeout(8000);*/
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        PermittedOptions inboundPerm = new PermittedOptions()
                .setAddress("some-address");
        PermittedOptions outboundPerm = new PermittedOptions()
                .setAddress("some-address");

        SockJSBridgeOptions bridgeOptions = new SockJSBridgeOptions().setPingTimeout(6000)
                .addInboundPermitted(inboundPerm)
                .addOutboundPermitted(outboundPerm);


        sockJSHandler.getDelegate().bridge(bridgeOptions, be -> {});
        router.mountSubRouter("/eventbus", sockJSHandler.bridge(bridgeOptions, be -> {
            System.out.println("#####");
            System.out.println(" SOCKET EVENT: "+ be.type());
            System.out.println(" SOCKET ADDRESS: "+ be.type());
            if(be.getRawMessage() != null)
                System.out.println(" BODY: "+be.getRawMessage().encode());
            be.complete(true);
        }));
        return router;
    }

    public Router mountRealtimeBus(){
        Router router = Router.router(vertx);
        router.route("/realtime/*")
                .handler(CorsHandler.create("*").allowCredentials(true))

        ;

        SockJSHandlerOptions options = new SockJSHandlerOptions()
               // .setRegisterWriteHandler(true)
                .setSessionTimeout(5000)
                .setHeartbeatInterval(2000) ;

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);
        router.mountSubRouter("/realtime/:token", sockJSHandler.socketHandler(sockJSSocket -> {
            HttpServerRequest request = sockJSSocket.routingContext().request();
            String accessToken = request.getParam("token");

            /**
             *  token payload: {
             *      topic_path::String,
             *      permission::String::{'r', 'rw'},
             *      user_id::Long,
             *      user_pref_name::String
             *  }
             *
             *
             *
             * */
            Integer socketHashCode = sockJSSocket.getDelegate().hashCode();
            System.out.println("### CONNECTED "+socketHashCode);
            sockJSSocket.closeHandler( ($) -> {
                System.out.println("### CLOSING SOCKET: "+socketHashCode);
            });
            sockJSSocket.handler( buffer -> {

                JsonObject data = new JsonObject(buffer.getDelegate());
                System.out.println("### received: "+data.encode() + "on "+socketHashCode);
                sockJSSocket.write(new JsonObject().put("### RECEIVED FOR SOCKET ", socketHashCode).encode());
            });
            // Just echo the data back
            //sockJSSocket.handler(sockJSSocket::write);

        }));
        return router;
    }
}