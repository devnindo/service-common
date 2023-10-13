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


import io.devnindo.datatype.util.JsonUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

public class DataSigner
{
    public static final Base64.Encoder BASE64_ENCODER;
    public static final Base64.Decoder BASE64_DECODER;

    static {

        BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
        BASE64_DECODER = Base64.getUrlDecoder();

    }
    public static enum Algo {
        HS256("HmacSHA256"),
        HS384("HmacSHA384"),
        HS512("HmacSHA512")
        ;

        String name;
        Algo(String name$){
            name = name$;
        }
    }

    private final Mac hmacAlgo;

    public DataSigner(Mac hmacAlgo$) {
        hmacAlgo = hmacAlgo$;
    }

    public static DataSigner init(Algo signAlgo, String secret) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        Mac hmacAlgo = null;
        try {
            hmacAlgo = Mac.getInstance(signAlgo.name);
            SecretKeySpec secretKey = new SecretKeySpec(secretBytes, signAlgo.name);
            hmacAlgo.init(secretKey);
            return new DataSigner(hmacAlgo);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }

    }

    public String sign(String data){
        Objects.requireNonNull(data);
        byte[] signedBytes = hmacAlgo.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return JsonUtil.BASE64_ENCODER.encodeToString(signedBytes);
    }
    public boolean verify(String data, String signedData){
        Objects.requireNonNull(signedData);
        String toCheck = sign(data);
        return toCheck.equals(signedData);
    };
}
