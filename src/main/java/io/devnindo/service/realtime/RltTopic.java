package io.devnindo.service.realtime;

import io.devnindo.datatype.schema.DataBean;

import java.time.Instant;

public class RltTopic implements DataBean
{
    Long userId;
    String topicId;
    Instant expireUTC;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public Instant getExpireUTC() {
        return expireUTC;
    }

    public void setExpireUTC(Instant expireUTC) {
        this.expireUTC = expireUTC;
    }
}
