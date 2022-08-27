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
 * @author prevy-sage
 */
public interface BizLogalyzerApi {
    public Single<JsonObject> logActionData(JsonObject reqData$);
}
