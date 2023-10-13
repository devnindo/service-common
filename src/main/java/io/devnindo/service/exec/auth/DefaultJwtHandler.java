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
    JwtConfigSpec configSpec;


    static {
        String header = new JsonObject()
                .put("typ", "jwt")
                .put("alg", "HS256").encode();
        encodedHeader = DataSigner.BASE64_ENCODER.encodeToString(header.getBytes());
    }

    private final DataSigner dataSigner;

    public DefaultJwtHandler(JwtConfigSpec configSpec$)
    {

        configSpec = configSpec$;
        dataSigner = DataSigner.init(DataSigner.Algo.HS256, configSpec$.getSecret());
    }


    @Override
    public String generateJWT(JsonObject data) {

        return generateJWT0(data, configSpec.getExpireInSec());
    }

    @Override
    public String generateJWT(JsonObject data, Long expireInSeconds){


        return generateJWT0(data, expireInSeconds);
    }

    @Override
    public Either<Violation, JsonObject> validateJWT(String jwToken) {
        String splits[] = jwToken.split("\.");
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
        if(model.isExpired())
            return Either.left(Violation.of("JWT_EXPIRE_TIME"));

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
                configSpec.getIssuer());

        return encodeJWT0(model.toJson().encode());
    }
    private String encodeJWT0(String payload){
        String encodedPayload = JsonUtil.BASE64_ENCODER.encodeToString (payload.getBytes());
        String encodedData = encodedHeader+"."+encodedPayload;
        return encodedData+"."+dataSigner.sign(encodedData);

    }

}
