package io.devnindo.service.exec.action.request;

import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.auth.BizUser;
import io.devnindo.datatype.json.Jsonable;;
import io.devnindo.datatype.json.JsonObject;

import java.time.Instant;

public class BizRequest implements Jsonable
{
    public final String serviceId;
    public final BizAction.Identity actionIdentity;
    public final BizUserClientInfo clientInfo;
    public final BizUser bizUser;
    public final JsonObject reqData;
    public final Instant reqDatime ;

    public BizRequest(String serviceId$, BizAction.Identity actionIdentity$, BizUserClientInfo clientInfo$, BizUser bizUser$, JsonObject reqData$) {

        this.serviceId = serviceId$;
        this.actionIdentity = actionIdentity$;
        this.clientInfo = clientInfo$;
        this.bizUser = bizUser$;
        this.reqData = reqData$;
        this.reqDatime = Instant.now();
    }

    @Override
    public  JsonObject toJson() {
        return new JsonObject()
                .put("service_id", serviceId)
                .put("action_id", actionIdentity.actionID)
                .put("action_type", actionIdentity.actionType)
                .put("client_info", clientInfo.toJson())
                .put("biz_user", bizUser.toJson())
                .put("req_data", reqData)
                .put("req_datime", reqDatime);
    }
}
