package io.devnindo.service.exec.auth;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.service.exec.action.request.BizAccessInfo;

public class TestJWT
{
    private final static JwtConfig config = new JwtConfig()
            .setAlgo("HS256")
            .setSecret("এই গোপন কথা zeno keu na jane")
            .setExpireInSec(60 * 60 * 24 * 365L)
            .setIssuer("devnindo-auth");
    public static void main(String... args){

        JWTSessionHandler sessionHandler = new JWTSessionHandler(config);
        BizAccessInfo accessInfo = new BizAccessInfo();
        accessInfo.setAccessToken("Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7InJvbGUiOiJOT1RfQVBQTElDQUJMRSIsImNyZWF0ZWRfZGF0aW1lIjpudWxsLCJwcmVmX25hbWUiOiJBTk9OWU1PVVMgZWFjYmE1NzUtMzMyMC00ZTNhLWFlZWItMTMzZTQ4YmQyMjdhIiwic3RhdHVzIjoicl9hbm9ueW1vdXMiLCJjaGFubmVsX2lkIjoibG9qZW5zIiwidXNlcl9pZCI6LTEsImRvbWFpbl9pZCI6ImVsZWFybmluZyJ9LCJpc3MiOiJkZXZuaW5kby1hdXRoIiwiaWF0IjoiMjAyMi0xMC0xOFQxNDoyNDowMS4yODA2MzE4MDBaIiwiZXhwIjoiMjAyMy0xMC0xOFQxNDoyNDowMS4yODA2MzE4MDBaIn0.Q_CIbmpCdStO2jKav2MEE07SiQcFJaF-zDOK57DK3nU");
        sessionHandler.doBiz(accessInfo).subscribe(either -> {
            Either<Violation, Void> authEither = BizAuth.REFERENCE_ACCESS_AUTH.checkAccess(either.right());
            System.out.println("# ACCESS ALLOWED: "+authEither.isRight());
            System.out.println(either.right().toJson().encodePrettily());
        });

        if(true) return;;

        JwtHandler handler = new DefaultJwtHandler(config);
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
