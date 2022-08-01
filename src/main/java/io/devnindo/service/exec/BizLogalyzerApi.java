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
public interface BizLogalyzerApi {
    public Single<JsonObject> logActionData(JsonObject reqData$);
}
