
import io.devnindo.service.auth.BizUser;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class BizUserTest
{
    @Test @Disabled
    public void testBuilder()
    {
        /*BizUser bizUser = BizUserBuilder.init()
                .userId(37L)
                .fullName("Rashed Alam")
                .prefName("Rashed")
                .role("admin")
                .domain("lojens")
                .channel("lojens")
                .channelClient("lojens-web")
                .build();*/

        JsonObject data = new JsonObject("{\n" +
                "  \"user_id\": \"86L\",\n" +
                "  \"uid\": \"866L\",\n" +
                "  \"full_name\": \"Muhammad Rashed Alam\",\n" +
                "  \"pref_name\": \"Rashed\",\n" +
                "  \"signed_datime\": \"2020-05-12T00:04:35.048Z\",\n" +
                "  \"status\": \"reg_user\",\n" +
                "  \"role\": \"user\",\n" +
                "  \"channel_id\": \"lojens\",\n" +
                "  \"domain_id\": \"generic-elearning-app\",\n" +
                "  \"channel_client_id\": \"lojens-web-app\",\n" +
                "  \"iat\": 1589241875,\n" +
                "  \"exp\": 1620777875,\n" +
                "  \"iss\": \"devnindo-auth\",\n" +
                "  \"cap_letter\": \"R\"\n" +
                "}");

        Either<Violation, BizUser> bizUserEither = BeanSchema.of(BizUser.class).apply(data);
        BizUser bizUser;
        if(bizUserEither.isLeft())
        {
            System.out.println(bizUserEither.left().toJson().encodePrettily());
            return;
        }
        bizUser = bizUserEither.right();
        Assertions.assertEquals("Rashed", bizUser.getPrefName());
        Assertions.assertEquals(86L, bizUser.getUserId());

    }
}
