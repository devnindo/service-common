package io.devnindo.service.exec.auth;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

public class JWTConfig implements DataBean
{
    @Required
    protected String issuer;

    @Required
    protected String secret;

    protected String algorithm;

    @Required
    protected Integer expireInSeconds;

    public String getIssuer() {
        return issuer;
    }

    public String getSecret() {
        return secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Integer getExpireInSeconds() {
        return expireInSeconds;
    }

}
