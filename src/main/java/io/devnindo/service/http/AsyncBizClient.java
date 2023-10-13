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
