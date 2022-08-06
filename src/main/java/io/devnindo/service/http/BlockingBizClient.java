/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.http;

import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.json.JsonObject;

/**
 *
 * @author prevy-sage
 */
public abstract class BlockingBizClient {
    
    public final String apiLocation;

    public BlockingBizClient(String apiLocation) {
        this.apiLocation = apiLocation;
    }
    
    public Either<JsonObject, JsonObject> invoke(String action$, JsonObject reqData$)
    {
        return null;
        
    }
}
