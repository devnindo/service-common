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

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

import java.time.Instant;

public class JwtModel implements DataBean
{
    Instant iat;
    @Required
    Instant exp;
    JsonObject data; // no standard, contain token data

    @Required
    String iss;

    public static final JwtModel init(Long expSeconds$, JsonObject data$, String iss$)
    {
        JwtModel model = new JwtModel();
        model.iat = Instant.now();
        model.exp = Instant.now().plusSeconds(expSeconds$);
        model.data = data$;
        model.iss = iss$;
        return model;
    }

    public boolean isExpired(){
        return Instant.now().isAfter(exp);
    }
    public Instant getIat() {
        return iat;
    }

    public Instant getExp() {
        return exp;
    }

    public JsonObject getData() {
        return data;
    }

    public String getIss() {
        return iss;
    }
}
