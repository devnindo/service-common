package io.devnindo.service.testunit.module_example.dummy;

import io.devnindo.datatype.schema.DataBean;

public class DummyData implements DataBean
{
    String name;
    Long sleepingTime;

    String reply;

    public String getName() {
        return name;
    }

    public Long getSleepingTime() {
        return sleepingTime;
    }

    public String getReply() {
        return reply;
    }
}
