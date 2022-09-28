package io.devnindo.service.exec.auth;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;

public interface JwtHandlerIF
{
    public String generateJWT(JsonObject payload, Integer expireInSeconds);
    public String generateJWT(JsonObject payload);
    public Either<Violation, JsonObject> validateJWT(String jwToken);

    public <T extends DataBean> Either<Violation, T> validateJWT(String jwToken, Class<T> dataClz);

}
