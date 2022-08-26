package io.devnindo.service.exec.action.request;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;

public class BizAccessInfo implements DataBean
{
     
    String accessToken;


     
    BizUserClientInfo clientInfo; // screen-size, os+version, ip-address


    public String getAccessToken() {
        return accessToken;
    }

    public BizUserClientInfo getClientInfo() {
        return clientInfo;
    }

}
