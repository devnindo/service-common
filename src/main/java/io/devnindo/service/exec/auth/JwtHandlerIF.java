package io.devnindo.service.exec.auth;

import io.devnindo.core.json.JsonObject;
import io.devnindo.core.util.Either;
import io.devnindo.core.validation.Violation;

public interface JwtHandlerIF
{
    public String generateJWT(JsonObject payload, Integer expireInSeconds);
    public String generateJWT(JsonObject payload);
    public Either<Violation, JsonObject> validateJWT(String jwToken);

}
