package io.devnindo.service.exec;

import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.core.json.Jsonable;;
import io.devnindo.core.json.JsonObject;

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
