package io.devnindo.service.exec.action.request;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;

public class BizUserClientInfo implements DataBean
{
     
    String ip;
     
    String userAgent;

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public BizUserClientInfo setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public BizUserClientInfo setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
}
