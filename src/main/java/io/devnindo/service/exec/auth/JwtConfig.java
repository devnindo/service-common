package io.devnindo.service.exec.auth;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;


/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
public class JwtConfig implements DataBean, JwtConfigSpec
{
    @Required
    protected String issuer;

    @Required
    protected String secret;

    protected String algo;

    @Required
    protected Long expireInSec;

    public String getIssuer() {
        return issuer;
    }

    public String getSecret() {
        return secret;
    }

    public String getAlgo() {
        return algo;
    }

    public Long getExpireInSec() {
        return expireInSec;
    }

    public JwtConfig setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public JwtConfig setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public JwtConfig setAlgo(String algorithm) {
        this.algo = algorithm;
        return this;
    }

    public JwtConfig setExpireInSec(Long expireInSec) {
        this.expireInSec = expireInSec;
        return this;
    }
}
