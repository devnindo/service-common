/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.dev;

import io.devnindo.service.exec.BizErrorLog;
import io.devnindo.service.exec.BizManagerApi;
import io.devnindo.datatype.json.JsonObject;
import io.reactivex.rxjava3.core.Single;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
@Singleton
public class DummyManagerApi implements BizManagerApi
{

    @Inject
    DummyManagerApi(){

    }
    @Override
    public Single<JsonObject> registerService(JsonObject jo) {
        System.out.println("# PENDING IMPLEMENTATION: A central registration service");
        return Single.just(new JsonObject());
    }


    @Override
    public Single<JsonObject> logError(BizErrorLog errorLog) {
        errorLog.printOnConsole();
        return Single.just(errorLog.toJson());
    }
    
}
