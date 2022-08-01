package io.devnindo.service.exec.auth;

import io.devnindo.core.json.JsonObject;
import io.devnindo.core.util.Either;
import io.devnindo.core.validation.Violation;

public class TestJWT
{
    public static void main(String... args){
        JWTConfig jwtConfig = new JWTConfig();
        jwtConfig.algorithm = "HS256";
        jwtConfig.issuer = "devnindo-auth";
        jwtConfig.secret = "ei-secret-jate-keu-na-jane";
        jwtConfig.expireInSeconds = 60*60*24;

        JwtHandlerIF handler = new DefaultJwtHandler(jwtConfig);
        JsonObject js = new JsonObject().put("id", "123").put("name", "Rashed");

        String token = handler.generateJWT(js);
        System.out.println(token);
        Either<Violation, JsonObject> jwtEither = handler.validateJWT(token);
     //   System.out.println(jwtEither.left().toJson().encodePrettily());
       System.out.println(jwtEither.right().encodePrettily());


    }
}
