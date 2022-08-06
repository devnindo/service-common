/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.dev;

import io.devnindo.service.exec.BizErrorLog;
import io.devnindo.service.exec.BizManagerApi;
import io.reactivex.Single;
import io.devnindo.datatype.json.JsonObject;

/**
 *
 * @author prevy-sage
 */
public class DevManagerApi implements BizManagerApi
{

    @Override
    public Single<JsonObject> registerService(JsonObject jo) {
        System.out.println("dummy registering service: \n"+jo.encodePrettily());
        return Single.just(new JsonObject());
    }

    @Override
    public Single<JsonObject> logServiceDown(JsonObject jo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Single<JsonObject> logServiceHealth(JsonObject jo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Single<JsonObject> logError(BizErrorLog errorLog) {
        errorLog.printOnConsole();
        return Single.just(errorLog.toJson());
    }
    
}
