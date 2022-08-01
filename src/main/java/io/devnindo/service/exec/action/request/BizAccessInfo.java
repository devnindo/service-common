package io.devnindo.service.exec.action.request;

import io.devnindo.core.schema.DataBean;
import io.devnindo.core.schema.AField;

public class BizAccessInfo implements DataBean
{
    @AField
    String accessToken;


    @AField
    BizUserClientInfo clientInfo; // screen-size, os+version, ip-address


    public String getAccessToken() {
        return accessToken;
    }

    public BizUserClientInfo getClientInfo() {
        return clientInfo;
    }

}
