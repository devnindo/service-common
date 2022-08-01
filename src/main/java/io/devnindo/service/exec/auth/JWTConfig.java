package io.devnindo.service.exec.auth;

import io.devnindo.core.schema.AField;
import io.devnindo.core.schema.DataBean;
import io.devnindo.core.schema.Required;

public class JWTConfig implements DataBean
{
    @AField @Required
    protected String issuer;

    @AField @Required
    protected String secret;

    @AField
    protected String algorithm;

    @AField @Required
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
