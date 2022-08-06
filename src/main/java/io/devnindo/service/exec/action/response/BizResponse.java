/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.exec.action.response;

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.Jsonable;;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.json.JsonObject;

import java.time.Instant;

/**
 *
 * @author prevy-sage
 */
public class BizResponse implements Jsonable
{
    final Object data;
    public final Status status;  
    protected static final JsonObject EMPTY_LOGGABLE = new JsonObject();
    public final Instant responseDatime = Instant.now();

    public static enum Status
    {
        SUCCESS(200),
        BIZ_FAILED(422),
        ACTION_NOT_FOUND(404),
        INTERNAL_ERROR(500),
        BAD_REQUEST(400);

        public final int code;
        Status(int code$)
        {
            code = code$;
        }
    }
//    public BizResponse(Object merged$, Status status$) {
//        merged = merged$;
//        status = status$;
//        loggableData = merged$;
//    }
    
    public BizResponse(Object data$, Status status$)
    {
        data = data$;
        status = status$;
    }


    public static BizResponse failed(Violation reportData$)
    {
        return new BizResponse(reportData$.toJson(), Status.BIZ_FAILED);
    }

    public static BizResponse error(JsonObject errorData$){
        return  new BizResponse(errorData$, Status.INTERNAL_ERROR);
    }

    public static BizResponse success(JsonObject data$)
    {
        return new BizResponse(data$, Status.SUCCESS);
    }

    public static BizResponse success(JsonArray data$)
    {
        return new BizResponse(data$, Status.SUCCESS);
    }
    
   // public static final BizResponse DATA_NOT_FOUND = new BizResponse(null);
    
    public boolean isSuccess()
    {
        return Status.SUCCESS.equals(status);
    }
    public boolean isFailed()
    {
        return !Status.SUCCESS.equals(status);
    } 
    
    public final JsonObject dataObj(){return (JsonObject) data; }
    public final JsonArray dataArray(){return (JsonArray) data; }
    public final boolean isDataObj(){return data instanceof JsonObject; }


    @Override
    public JsonObject toJson() {
        
        return new JsonObject().put("status", status.name())
                .put("status_code", status.code)
                .put("data", data);
    }

    public JsonObject loggableJson(){
        return new JsonObject()
                .put("resp_status", status.name())
                .put("resp_datime", responseDatime);

    }
    
    
    public static final BizResponse ACTION_NOT_FOUND = new BizResponse(new JsonObject(), Status.ACTION_NOT_FOUND);
    public static final BizResponse INTERNAL_ERROR = new BizResponse(new JsonObject(), Status.INTERNAL_ERROR) ;
    public static final BizResponse BAD_REQUEST = new BizResponse(new JsonObject(), Status.BAD_REQUEST);
}
