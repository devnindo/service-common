package io.devnindo.service.exec.auth;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

import java.time.Instant;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
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
