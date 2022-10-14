package io.devnindo.service.exec.auth;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;

import java.time.Instant;

public class JwToken implements DataBean
{
    Instant iat;
    Instant exp;
    JsonObject data; // no standard, contain token data

    String iss;

    public JwToken(Instant exp$, JsonObject data$, String iss$)
    {
        iat = Instant.now();
        exp = exp$;
        data = data$;
        iss = iss$;
    }

    public boolean isExpired(){
        return Instant.now().isBefore(exp);
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
