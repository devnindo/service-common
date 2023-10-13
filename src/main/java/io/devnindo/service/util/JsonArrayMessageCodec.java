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
package io.devnindo.service.util;

import io.devnindo.datatype.json.JsonArray;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class JsonArrayMessageCodec implements MessageCodec<JsonArray, JsonArray>
{
    @Override
    public void encodeToWire(Buffer buffer, JsonArray json) {
        // Encode object to string
        byte[] byteData = json.toByteData();
        // Length of JSON: is NOT characters count
        buffer.appendInt(byteData.length);
        buffer.appendBytes(byteData);
    }

    @Override
    public JsonArray decodeFromWire(int pos, Buffer buffer) {
        // Length of JSON
        int length = buffer.getInt(pos);
        // Get JSON byte data by it`s length
        // Jump 4 because getInt() == 4 bytes
        byte[] byteData = buffer.getBytes(pos+=4, pos+=length);
        return new JsonArray(byteData);
      }

    @Override
    public JsonArray transform(JsonArray json) {
        return json;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
