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
package io.devnindo.service.exec;

import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.datatype.json.Jsonable;;
import io.devnindo.datatype.json.JsonObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BizErrorLog implements Jsonable {


    public final BizRequest bizRequest;
    public final BizException bizException;
    public static final   DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E, dd L yyyy HH:mm a");
    public BizErrorLog(BizRequest bizRequest, BizException bizException) {
        this.bizRequest = bizRequest;
        this.bizException = bizException;
    }

    public void printOnConsole(){
        System.out.println("==ON ACTION: "+bizRequest.actionIdentity.actionID);
        System.out.println("==ON STEP: "+bizException.stepName());
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(bizException.exceptionDatime(), ZoneId.systemDefault());
        System.out.println("==ERROR TIME: "+dateTime.format(DATE_FORMATTER.RFC_1123_DATE_TIME));
        bizException.printStackTrace();

    }

    @Override
    public JsonObject toJson() {
        return bizRequest.toJson()
                .mergeIn(bizException.toJson());
    }
}
