package io.devnindo.service.exec.auth;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;

public class TestJWT
{
    public static void main(String... args){
        JwtConfig jwtConfig = new JwtConfig()
                .setAlgorithm("HS256")
                .setIssuer("devnindo-auth")
                .setSecret("ei-secret-jate-keu-na-jane")
                .setExpireInSeconds(60L);

        JwtHandler handler = new DefaultJwtHandler(jwtConfig);
        JsonObject js = new JsonObject().put("id", "123").put("name", "Rashed");

        String token = handler.generateJWT(js);
        System.out.println(token);
        Either<Violation, JsonObject> jwtEither = handler.validateJWT(token);
        System.out.println(jwtEither.right().encodePrettily());
        try{
            Thread.sleep(61*1000);
            // retry validating after 61 seconds
            jwtEither = handler.validateJWT(token);
            System.out.println(jwtEither.left().toJson().encodePrettily());
        }catch (InterruptedException excp){

        }

     //   System.out.println(jwtEither.left().toJson().encodePrettily());


    }
}
