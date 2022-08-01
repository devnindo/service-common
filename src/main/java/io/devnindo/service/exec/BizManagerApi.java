/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.exec;

import io.reactivex.Single;
import io.devnindo.core.json.JsonObject;

/**
 *
 * @author prevy-sage
 */
public interface BizManagerApi {
     
    public Single<JsonObject> registerService(JsonObject reqData$);

    public Single<JsonObject> logServiceDown(JsonObject reqData$);
    
    public Single<JsonObject> logServiceHealth(JsonObject reqData$);

    public Single<JsonObject> logError(BizErrorLog errorLog$);
}
