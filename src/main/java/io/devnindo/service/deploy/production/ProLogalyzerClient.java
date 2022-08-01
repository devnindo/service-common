/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.production;

import io.devnindo.service.http.AsyncBizClient;
import io.devnindo.service.configmodels.ConfigApiClient;
import io.devnindo.service.exec.BizLogalyzerApi;
import io.devnindo.service.util.DeployParams;
import io.reactivex.Single;
import io.devnindo.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.WebClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;


/**
 *
 * @author prevy-sage
 */
@Singleton
public class ProLogalyzerClient extends AsyncBizClient implements BizLogalyzerApi {
    
    @Inject
    public ProLogalyzerClient(
            @Named(DeployParams.LOG_ANALYZER_API)
                    ConfigApiClient apiConfig$,
            
            WebClient webClient$) {
        super(apiConfig$, webClient$);
    }
    
    public Single<JsonObject> logActionData(JsonObject reqData$)
    {
       return this.invoke("action.command.LogRequest", reqData$);
    }
    
}
