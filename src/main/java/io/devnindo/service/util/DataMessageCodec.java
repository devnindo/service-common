package io.devnindo.service.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class DataMessageCodec implements MessageCodec<DataSharable, DataSharable>
{
    @Override
    public void encodeToWire(Buffer buffer, DataSharable dataSharable) {

    }

    @Override
    public DataSharable decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    @Override
    public DataSharable transform(DataSharable dataSharable) {
        return null;
    }

    @Override
    public String name() {
        return this.getClass().getName();
    }

    @Override
    public byte systemCodecID() {
        return 0;
    }
}
