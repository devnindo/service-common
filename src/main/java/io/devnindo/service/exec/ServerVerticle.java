package io.devnindo.service.exec;


import io.devnindo.datatype.util.Values;
import io.devnindo.service.configmodels.ParamHttp;
import io.devnindo.service.configmodels.ConfigServer;
import io.devnindo.service.configmodels.ParamService;
import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.request.BizAccessInfo;
import io.devnindo.service.exec.action.request.BizUserClientInfo;
import io.devnindo.datatype.util.ClzUtil;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.core.http.HttpServerResponse;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;


public class ServerVerticle extends AbstractVerticle {


    private final BizExecutor executor;
    private final ConfigServer serverConfig;
    private final HttpServerOptions serverOps;

    public ServerVerticle(BizExecutor executor$, ConfigServer serverConfig$) {

        serverConfig = serverConfig$;
        executor = executor$;
        serverOps = new HttpServerOptions()
                .setPort(serverConfig$.getPort())
                .setCompressionSupported(true)
                .setSsl(serverConfig.getSslEnabled());
        if(Boolean.TRUE.equals(serverConfig$.getSslEnabled())){
            serverOps.setPemKeyCertOptions(new PemKeyCertOptions()
                     .setCertPath(serverConfig.getSslCertLocation())
                      .setKeyPath(serverConfig.getSslKeyLocation()));
        }
    }


     @Override
    public void start(Promise<Void> startFuture) throws Exception {


          Router router = Router.router(vertx);
    //    System.out.println("Starting Verticle: "+ServerVerticle.class.getName());

         router.route().handler(BodyHandler.create())
                      .handler(CorsHandler.create("*")
                           .allowedMethod(HttpMethod.POST)
                           .allowedHeader(ParamHttp.CONTENT_TYPE)
                          .allowedHeader(ParamHttp.AUTHORIZATION)
                          .allowedHeader(ParamHttp.X_VIEWPORT)
                          .allowedHeader(ParamHttp.USER_AGENT)
                          .allowedHeader(ParamHttp.X_CSRF_TOKEN));


         router.post("/api/:" + ParamService.ACTION_ID)
             .consumes(ParamHttp.CONTENT_JSON)
             .handler(this.routeHandler());

//         setUploadRoute(router);

         vertx.createHttpServer(serverOps)
             .requestHandler(router)
             .listen()
             .subscribe((server) -> {
                 startFuture.complete();
                 System.out.println("# HTTP Server deployed with port: " + server.actualPort());
             }, (err)->{
                 System.out.println("# Server Deploy Failed");
                 err.printStackTrace();
             });

         //startFuture.complete();
     }



    private Handler<RoutingContext> routeHandler() {
        return routingCtx -> {
            HttpServerResponse httpResponse = routingCtx.response();
            httpResponse.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            String actionIdName = routingCtx.request().getParam(ParamService.ACTION_ID);


            String reqData = routingCtx.body().getDelegate().asString();
            JsonObject reqBody = new JsonObject(reqData);
            JsonObject accessInfo = getAccessInfo0(routingCtx);


                JsonObject bizRequest = new JsonObject()
                        .put(ParamService.ACTION_ID, actionIdName)
                        .put(ParamService.ACCESS_INFO, accessInfo)
                        .put(ParamService.REQUEST_DATA, reqBody);

                executor.executeOn(bizRequest)
                        .subscribe(srvResponse -> {
                            httpResponse.setStatusCode(srvResponse.status.code);
                            httpResponse.end(srvResponse.toJson().encode());
                        }, error -> {
                            if(error instanceof BizException){
                               System.out.println(BizException.class.cast(error).toJson().encodePrettily());
                            }

                            httpResponse.setStatusCode(500);
                            httpResponse.end(ClzUtil.throwableString(error));
                        });


        };
    }
    private JsonObject getAccessInfo0(RoutingContext routingCtx$)
    {


        HttpServerRequest httpReq = routingCtx$.request();

        String accessToken = httpReq.getHeader(ParamHttp.AUTHORIZATION);
        if(accessToken==null)
        {
            accessToken = Values.NOT_AVAILABLE;
        }
        String reqIp = httpReq.remoteAddress().host();
        String agentInfo = httpReq.getHeader(ParamHttp.USER_AGENT);

        BizAccessInfo accessInfo = new BizAccessInfo();
        BizUserClientInfo clientInfo = new BizUserClientInfo();
        clientInfo
                .setIp(reqIp)
                .setUserAgent(agentInfo);

        return accessInfo.setAccessToken(accessToken)
                .setClientInfo(clientInfo)
                .toJson();
    }




}
