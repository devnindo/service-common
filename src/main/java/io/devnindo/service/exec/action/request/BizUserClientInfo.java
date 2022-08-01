package io.devnindo.service.exec.action.request;

import io.devnindo.core.schema.DataBean;
import io.devnindo.core.schema.AField;

public class BizUserClientInfo implements DataBean
{
    @AField
    String ip;


    @AField
    String userAgent;

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
