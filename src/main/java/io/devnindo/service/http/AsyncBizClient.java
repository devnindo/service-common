/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.http;

import io.devnindo.service.configmodels.ConfigApiClient;
import io.devnindo.datatype.json.JsonObject;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.client.HttpRequest;
import io.vertx.rxjava3.ext.web.client.WebClient;

import java.util.HashMap;
import java.util.Map;
//import io.vertx.ext.web.client.WebClient 

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
public abstract class AsyncBizClient {
    private final WebClient webClient ;//=  WebClient.create(ServiceMain.VERTX);
 
    protected final Integer port;
    protected final String host;
    private  String apiRoot;
    private final String transferProtocol;
    private final Boolean sslEnabled;
    private static final Map<String, HttpRequest<Buffer>> requestCache = new HashMap<>();
    
    public AsyncBizClient(ConfigApiClient config$, WebClient webClient$) {
//        if(apiLocation$.endsWith("/")==false)
//            apiLocation$ += "/";
//        apiLocation = apiLocation$;
          webClient = webClient$;
          port = config$.getPort();
          host = config$.getHost();
          String _apiRoot = config$.getApiRoot();
          sslEnabled = config$.getSslEnabled();
          
          if(sslEnabled)
              transferProtocol = "https";
          else transferProtocol = "http";
        if(_apiRoot==null)
            apiRoot = "";
         
        if(_apiRoot.startsWith("/"))
            apiRoot = _apiRoot.substring(1);
        if(_apiRoot.endsWith("/"))
            apiRoot = _apiRoot.substring(0, _apiRoot.length()-1);
       
    }
 
     
    /*
            Any service can return a success response with merged keyed as
            "merged" with domain type {JsonObject, JsonArray}. Hence invoke function
            returns Single<Object> instead of Single<JsonObject> or Single<JsonArray>
            in particular.
    */
    public final <T> Single<T> invoke(String action, JsonObject reData$){
             
     
       throw new UnsupportedOperationException();
        
       /* String actionURI = "/"+apiRoot+"/"+action;
        String completePath =  transferProtocol+"://"+host+":"+port+actionURI;
        
        HttpRequest<Buffer> request = requestCache.get(completePath);
        
        if(request == null)
        {
            request = webClient.post(port, host, actionURI)
                    .as(BodyCodec.buffer()).ssl(sslEnabled);
            
            requestCache.put(completePath, request);
        }

        Buffer.buffer()
        return request.rxSendJsonObject()
                .map(HttpResponse::body).map(js -> (T) js.getValue("data"));*/
        
    }
    
//    public void send(Handler<AsyncResult<HttpResponse<Buffer>>> handler$)
//    {
//        webClient.post(apiLocation).sendJsonObject(reqData, handler$);
//    }
    
    
}
