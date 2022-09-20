package io.devnindo.service.util;

import io.devnindo.datatype.json.JsonObject;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class JsonObjectMessageCodec implements MessageCodec<JsonObject, JsonObject>
{
    @Override
    public void encodeToWire(Buffer buffer, JsonObject json) {
        // Encode object to string

        System.out.println("EVENT BUS: LOCAL ENCODING DOES HAPPEN");
        // Length of JSON: is NOT characters count
        byte[] byteData = json.toByteData();
        buffer.appendInt(byteData.length);
        buffer.appendBytes(byteData);
    }

    @Override
    public JsonObject decodeFromWire(int pos, Buffer buffer) {

        System.out.println("EVENT BUS: LOCAL ENCODING DOES HAPPEN DURING WIRE");
        // Length of JSON
        int length = buffer.getInt(pos);

        // Get JSON byte-data by it`s length
        // Jump 4 because getInt() == 4 bytes
        byte[] byteData = buffer.getBytes(pos+=4, pos+=length);
        return new JsonObject(byteData);
      }

    @Override
    public JsonObject transform(JsonObject json) {
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
