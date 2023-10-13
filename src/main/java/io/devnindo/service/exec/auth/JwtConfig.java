/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.service.exec.auth;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;


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
