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

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.service.exec.action.request.BizAccessInfo;
import io.devnindo.service.exec.auth.BizAuth;
import io.devnindo.service.exec.auth.DefaultJwtHandler;
import io.devnindo.service.exec.auth.JwtConfig;
import io.devnindo.service.exec.auth.JwtHandler;

public class TestJWT
{
//     /*private final static JwtConfig config = new JwtConfig()
//             .setAlgo("HS256")
//             .setSecret("এই গোপন কথা zeno keu na jane")
//             .setExpireInSec(60 * 60 * 24 * 365L)
//             .setIssuer("devnindo-auth");
//     */

//     private final static JwtConfig config = new JsonObject("{
// " +
//             "    \"issuer\": \"devnindo-auth\",
// " +
//             "    \"secret\": \"এই গোপন কথা zeno keu na jane\",
// " +
//             "    \"algo\": \"HS256\",
// " +
//             "    \"expire_in_sec\": 31536000
// " +
//             "  }").toBean(JwtConfig.class);
//     public static void main(String... args){

//         JWTSessionHandler sessionHandler = new JWTSessionHandler(config);
//         BizAccessInfo accessInfo = new BizAccessInfo();
//         accessInfo.setAccessToken("Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7InJvbGUiOiJOT1RfQVBQTElDQUJMRSIsImNyZWF0ZWRfZGF0aW1lIjpudWxsLCJwcmVmX25hbWUiOiJBTk9OWU1PVVMgMzAxNzVjY2QtYWU2Mi00ZTZiLWI3NjctYjFiMmVmNmQzMzM2Iiwic3RhdHVzIjoicl9hbm9ueW1vdXMiLCJjaGFubmVsX2lkIjoibG9qZW5zIiwidXNlcl9pZCI6LTEsImRvbWFpbl9pZCI6ImVsZWFybmluZyJ9LCJpc3MiOiJkZXZuaW5kby1hdXRoIiwiaWF0IjoiMjAyMi0xMC0xOFQyMjowMzoyMS40MDY2MTM2MDBaIiwiZXhwIjoiMjAyMy0xMC0xOFQyMjowMzoyMS40MDY2MTM2MDBaIn0.QaabKvgWxRUSgtVsvA22FHXc3FkO_gDUU6aX_bA0vbk");
//         sessionHandler.doBiz(accessInfo).subscribe(either -> {
//             Either<Violation, Void> authEither = BizAuth.REFERENCE_ACCESS_AUTH.checkAccess(either.right());
//             System.out.println("# ACCESS ALLOWED: "+authEither.isRight());
//             System.out.println(either.right().toJson().encodePrettily());
//         });

//         if(true) return;;

//         JwtHandler handler = new DefaultJwtHandler(config);
//         JsonObject js = new JsonObject().put("id", "123").put("name", "Rashed");

//         String token = handler.generateJWT(js);
//         System.out.println(token);
//         Either<Violation, JsonObject> jwtEither = handler.validateJWT(token);
//         System.out.println(jwtEither.right().encodePrettily());
//         try{
//             Thread.sleep(61*1000);
//             // retry validating after 61 seconds
//             jwtEither = handler.validateJWT(token);
//             System.out.println(jwtEither.left().toJson().encodePrettily());
//         }catch (InterruptedException excp){

//         }

//      //   System.out.println(jwtEither.left().toJson().encodePrettily());


//     }
}
