package io.devnindo.service.exec.auth;

import io.devnindo.core.json.JsonObject;
import io.devnindo.core.json.impl.JsonImplUtil;
import io.devnindo.core.util.Either;
import io.devnindo.core.validation.Violation;

import java.time.Instant;
import java.util.Base64;

public class DefaultJwtHandler implements JwtHandlerIF
{

    private final static String  encodedHeader;

    static {
        String header = new JsonObject()
                .put("typ", "jwt")
                .put("alg", "HS256").encode();
        encodedHeader = DataSigner.BASE64_ENCODER.encodeToString(header.getBytes());
    }

    private final JWTConfig jwtConfig;
    private final DataSigner dataSigner;

    public DefaultJwtHandler(JWTConfig config$)
    {

        jwtConfig = config$;
        dataSigner = DataSigner.init(DataSigner.Algo.HS256, config$.getSecret());
    }


    @Override
    public String generateJWT(JsonObject data) {

        return generateJWT0(data, jwtConfig.getExpireInSeconds());
    }

    @Override
    public String generateJWT(JsonObject data, Integer expireInSeconds){


        return generateJWT0(data, expireInSeconds);
    }

    @Override
    public Either<Violation, JsonObject> validateJWT(String jwToken) {
        String splits[] = jwToken.split("\\.");
        if(splits.length < 3)
            return Either.left(Violation.withCtx("VALID_JWT", "VALID_FORMAT"));

        String header = splits[0];
        String payload = splits[1];
        String signature = splits[2];
        if(dataSigner.verify(header+"."+payload, signature) == false)
            return Either.left(Violation.withCtx("VALID_JWT", "VALID_SIGNATURE"));

        byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
        String decoded = new String(decodedBytes);
        JsonObject payloadJS = new JsonObject(decoded);

        return Either.right(payloadJS.getJsonObject("data"));
    }


    private String generateJWT0(JsonObject data, int expireInSeconds){
        JsonObject payload = new JsonObject();
        payload.put("exp", Instant.now().plusSeconds(expireInSeconds));
        payload.put("iat", Instant.now().getEpochSecond());
        payload.put("iss", jwtConfig.getIssuer());
        payload.put("data", data);
        return encodeJWT0(payload.encode());
    }
    private String encodeJWT0(String payload){
        String encodedPayload = JsonImplUtil.BASE64_ENCODER.encodeToString (payload.getBytes());
        String encodedData = encodedHeader+"."+encodedPayload;
        return encodedData+"."+dataSigner.sign(encodedData);

    }


}
