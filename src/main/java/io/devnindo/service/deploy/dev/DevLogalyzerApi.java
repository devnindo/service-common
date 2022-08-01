/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.dev;

import io.devnindo.service.exec.BizLogalyzerApi;
import io.reactivex.Single;
import io.devnindo.core.json.JsonObject;

/**
 *
 * @author prevy-sage
 */
public class DevLogalyzerApi implements BizLogalyzerApi {

    @Override
    public Single<JsonObject> logActionData(JsonObject jo) {
        //System.out.println("action-merged: \n"+jo.encodePrettily());
        return Single.just(new JsonObject());
    }
    
}
