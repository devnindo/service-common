package io.devnindo.service.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.core.shareddata.Shareable;

public class DataSharable<T> implements ClusterSerializable
{
    byte[] byteData;
    public DataSharable(T jsObj)
    {
        //byteData = jsObj.toByteData();
    }


    @Override
    public void writeToBuffer(Buffer buffer) {

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
