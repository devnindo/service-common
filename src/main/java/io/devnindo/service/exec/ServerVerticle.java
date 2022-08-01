package io.devnindo.service.exec;


import io.devnindo.service.configmodels.ParamHttp;
import io.devnindo.service.configmodels.ConfigServer;
import io.devnindo.service.configmodels.ParamService;
import io.devnindo.service.exec.action.request.$BizAccessInfo;
import io.devnindo.service.exec.action.request.$BizUserClientInfo;
import io.devnindo.service.util.Values;
import io.devnindo.core.util.ClzUtil;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.devnindo.core.json.JsonObject;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.CorsHandler;


public class ServerVerticle extends AbstractVerticle {


    private final BizExecutor executor;
    private final ConfigServer serverConfig;
    private final HttpServerOptions serverOps;

    public ServerVerticle(BizExecutor executor$, ConfigServer serverConfig$) {

        serverConfig = serverConfig$;
        executor = executor$;
        serverOps = new HttpServerOptions()
                .setCompressionSupported(true)
                .setSsl(serverConfig.getSslEnabled())
                .setPemKeyCertOptions(new PemKeyCertOptions()
                        .setCertPath(serverConfig.getSslCertLocation())
                        .setKeyPath(serverConfig.getSslKeyLocation()));
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
             .consumes(ParamHttp.CONTENT_APP_JSON)
             .handler(this.routeHandler());

//         setUploadRoute(router);

         vertx.createHttpServer(serverOps)
             .requestHandler(router)
             .listen(serverConfig.getPort(), (async) -> {
                 startFuture.complete();
                 System.out.println("HTTP Server deployed with port: " + async.result().actualPort());
             });

         //startFuture.complete();
     }



    private Handler<RoutingContext> routeHandler() {
        return routingCtx -> {
            HttpServerResponse httpResponse = routingCtx.response();
            httpResponse.putHeader("Content-Type", "application/json");

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
                            httpResponse.setStatusCode(500);
                            httpResponse.end(ClzUtil.throwableString(error));
                        });


        };
    }
    private JsonObject getAccessInfo0(RoutingContext routingCtx$)
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
        JsonObject clientInfo = new JsonObject()
                .put($BizUserClientInfo.IP, reqIp)
                .put($BizUserClientInfo.USER_AGENT, agentInfo);

        return accessInfo.put($BizAccessInfo.ACCESS_TOKEN, accessToken)
                .put($BizAccessInfo.CLIENT_INFO, clientInfo);

    }
/*
    @Deprecated
    private void setUploadRoute(Router router) {
        Route uploadRoute = router.post("/blob/upload/:" + ServiceParam.ACTION_ID);
        uploadRoute.handler(BodyHandler.create().setUploadsDirectory("/tmp"));
        uploadRoute.handler(this.uploadHandler());
    }
    @Deprecated
    private Handler<RoutingContext> uploadHandler() {
        return routingCtx -> {
            HttpServerResponse httpResponse = routingCtx.response();
            httpResponse.putHeader("Content-Type", "application/json");

            List<FileUpload> uploadedFileList = routingCtx.fileUploads();
            JsonObject metadata = new JsonObject();

            MultiMap metaAttributes = routingCtx.request().formAttributes();
            for (Map.Entry<String, String> entry : metaAttributes.entries()) {
                metadata.put(entry.getKey(), entry.getValue());
            }

            List<JsonObject> uploadedBlobInfo = uploadedFileList.stream().map(f -> {
                return new JsonObject()
                    .put("uploaded_file", new File(f.uploadedFileName()).getAbsolutePath())
                    .put("file_name", f.fileName())
                    .put("form_key", f.name())
                    .put("mime_type", f.contentType())
                    .put("size", f.size());
            }).collect(Collectors.toList());

            String actionIdName = routingCtx.request().getParam(ServiceParam.ACTION_ID);
            JsonObject reqBody = metadata.put("blob_info", uploadedBlobInfo);
            JsonObject accessInfo = getAccessInfo0(routingCtx);


            JsonObject bizRequest = new JsonObject()
                .put(ServiceParam.ACTION_ID, actionIdName)
                .put(ServiceParam.ACCESS_INFO, accessInfo)
                .put(ServiceParam.REQUEST_DATA, reqBody);

            executor.executeOn(bizRequest)
                .subscribe(srvResponse -> {
                    httpResponse.setStatusCode(srvResponse.status.code);
                    httpResponse.end(srvResponse.toJson().encode());
                }, error -> {
                    httpResponse.setStatusCode(500);
                    httpResponse.end(ClzUtil.throwableString(error));
                });


        };
    }*/




}
