package io.devnindo.service.exec.auth;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

public class JwtConfig implements DataBean
{
    @Required
    protected String issuer;

    @Required
    protected String secret;

    protected String algorithm;

    @Required
    protected Long expireInSeconds;

    public String getIssuer() {
        return issuer;
    }

    public String getSecret() {
        return secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Long getExpireInSeconds() {
        return expireInSeconds;
    }

    public JwtConfig setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public JwtConfig setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public JwtConfig setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public JwtConfig setExpireInSeconds(Long expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
        return this;
    }
}
