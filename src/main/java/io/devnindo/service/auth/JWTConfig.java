package io.devnindo.service.auth;

import io.devnindo.datatype.schema.AField;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

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
