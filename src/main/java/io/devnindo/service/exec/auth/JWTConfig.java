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

    public JWTConfig setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public JWTConfig setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public JWTConfig setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public JWTConfig setExpireInSeconds(Integer expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
        return this;
    }
}
