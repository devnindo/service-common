/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.service.realtime;


import io.devnindo.service.configmodels.ConfigServer;
import io.devnindo.service.configmodels.ParamHttp;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;
import io.vertx.rxjava3.ext.web.handler.sockjs.SockJSHandler;


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


        return router;
    }
}
