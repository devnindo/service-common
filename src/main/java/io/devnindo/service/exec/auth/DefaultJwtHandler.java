package io.devnindo.service.exec.auth;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.util.JsonUtil;
import io.devnindo.datatype.validation.Violation;

import java.time.Instant;
import java.util.Base64;

public class DefaultJwtHandler implements JwtHandler
{

    private final static String  encodedHeader;



    static {
        String header = new JsonObject()
                .put("typ", "jwt")
                .put("alg", "HS256").encode();
        encodedHeader = DataSigner.BASE64_ENCODER.encodeToString(header.getBytes());
    }

    private final JwtConfig jwtConfig;
    private final DataSigner dataSigner;

    public DefaultJwtHandler(JwtConfig config$)
    {

        jwtConfig = config$;
        dataSigner = DataSigner.init(DataSigner.Algo.HS256, config$.getSecret());
    }


    @Override
    public String generateJWT(JsonObject data) {

        return generateJWT0(data, jwtConfig.getExpireInSeconds());
    }

    @Override
    public String generateJWT(JsonObject data, Long expireInSeconds){


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
        JwtModel model = new JsonObject(decoded).toBean(JwtModel.class);
        if(model.isExpired()){
            System.out.println(model.getIat());
            System.out.println(model.getExp());
            System.out.println(Instant.now());
            return Either.left(Violation.of("JWT_EXPIRE_TIME"));
        }

        return Either.right(model.getData());
    }

    public <T extends DataBean> Either<Violation, T> validateJWT(String jwToken, Class<T> beanClz)
    {
        Either<Violation, JsonObject> dataEither = validateJWT(jwToken);
        if(dataEither.isLeft())
            return Either.left(dataEither.left());
        else
            return dataEither.right().toBeanEither(beanClz);
    }


    private String generateJWT0(JsonObject data, Long expireInSeconds){
        JwtModel model = JwtModel.init(
                expireInSeconds,
                data,
                jwtConfig.getIssuer());

        return encodeJWT0(model.toJson().encode());
    }
    private String encodeJWT0(String payload){
        String encodedPayload = JsonUtil.BASE64_ENCODER.encodeToString (payload.getBytes());
        String encodedData = encodedHeader+"."+encodedPayload;
        return encodedData+"."+dataSigner.sign(encodedData);

    }

}
