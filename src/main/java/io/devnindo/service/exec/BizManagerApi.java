/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.exec;

import io.devnindo.datatype.json.JsonObject;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
public interface BizManagerApi {
     
    public Single<JsonObject> registerService(JsonObject reqData$);


    public Single<JsonObject> logError(BizErrorLog errorLog$);
}
