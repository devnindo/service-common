/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.service.exec.action.response;

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.Jsonable;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.json.JsonObject;

import java.time.Instant;

public class BizResponse implements Jsonable
{
    final Object data;
    public final Status status;
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

    
    public static final BizResponse ACTION_NOT_FOUND = new BizResponse(new JsonObject(), Status.ACTION_NOT_FOUND);
    public static final BizResponse INTERNAL_ERROR = new BizResponse(new JsonObject(), Status.INTERNAL_ERROR) ;
    public static final BizResponse BAD_REQUEST = new BizResponse(new JsonObject(), Status.BAD_REQUEST);
}
