package io.devnindo.service.util;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.json.Jsonable;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.shareddata.ClusterSerializable;


public class DataSharable implements ClusterSerializable
{
    byte[] buf;
    public DataSharable(Jsonable dataObj)
    {
        //byteData = jsObj.toByteData();
        JsonObject js = new JsonObject();
        js.put("type", "JSONABLE");
        js.put("class", dataObj.getClass());
        js.put("data", dataObj.toJson());
        buf = js.toByteData();
    }


    @Override
    public void writeToBuffer(Buffer buffer) {
        buffer.appendInt(buf.length);
        buffer.appendBytes(buf);
    }

    @Override
    public int readFromBuffer(int pos, Buffer buffer) {
        return 0;
    }


    /*@Override
    public default void writeToBuffer(Buffer buffer) {
        Buffer buf = toBuffer();
        buffer.appendInt(buf.length());
        buffer.appendBuffer(buf);
    }

    @Override
    public default int readFromBuffer(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        int start = pos + 4;
        Buffer buf = buffer.getBuffer(start, start + length);
        fromBuffer(buf);
        return pos + length + 4;
    }*/
}
