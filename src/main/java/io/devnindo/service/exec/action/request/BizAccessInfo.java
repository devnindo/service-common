package io.devnindo.service.exec.action.request;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;

public class BizAccessInfo implements DataBean
{
     
    String accessToken;

    BizUserClientInfo clientInfo;

    public BizAccessInfo setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public BizUserClientInfo getClientInfo() {
        return clientInfo;
    }

    public BizAccessInfo setClientInfo(BizUserClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }



}
