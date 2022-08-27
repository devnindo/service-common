/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.production;

import io.devnindo.service.http.AsyncBizClient;
import io.devnindo.service.configmodels.ConfigApiClient;
import io.devnindo.service.exec.BizErrorLog;
import io.devnindo.service.exec.BizManagerApi;
import io.devnindo.service.util.DeployParams;
import io.devnindo.datatype.json.JsonObject;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.ext.web.client.WebClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 *
 * @author prevy-sage
 */
@Singleton
public class ProManagerClient extends AsyncBizClient implements BizManagerApi
{
    @Inject
    public ProManagerClient(
            @Named(DeployParams.MANAGER_API)
                    ConfigApiClient apiConfig$,
            
            WebClient webClient$) {
        super(apiConfig$, webClient$);
    }
    
    @Override
    public Single<JsonObject> registerService(JsonObject reqData$)
    {
        if("N/A".equals(host))
         return Single.just(new JsonObject().put("message", "Manager has not been deployed yet :)"));
       return this.invoke("manager.command.RegisterService", reqData$);
    }
    
    @Override
    public Single<JsonObject> logServiceDown(JsonObject reqData$)
    {
       return this.invoke("manager.command.LogServiceDown", reqData$);
    }
    
    @Override
    public Single<JsonObject> logServiceHealth(JsonObject reqData$)
    {
       return this.invoke("manager.command.LogServiceHealth", reqData$);
    }
    
    @Override
    public Single<JsonObject> logError(BizErrorLog errorLog$)
    {
       return this.invoke("manager.command.LogException", errorLog$.toJson());
    }
}
