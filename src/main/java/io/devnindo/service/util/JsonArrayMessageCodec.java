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
