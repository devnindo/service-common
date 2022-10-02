package io.devnindo.service.realtime;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

import java.security.Permission;
import java.time.Instant;

public class RltTopic implements DataBean
{
    @Required
    Long userId;
    @Required
    String topicId;
    Integer maxAge; // in seconds
    @Required
    RltPermission permission;

    public Long getUserId() {
        return userId;
    }

    public RltTopic setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getTopicId() {
        return topicId;
    }

    public RltTopic setTopicId(String topicId) {
        this.topicId = topicId;
        return this;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public RltTopic setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public RltPermission getPermission() {
        return permission;
    }

    public RltTopic setPermission(RltPermission permission) {
        this.permission = permission;
        return this;
    }

    public boolean canUserWrite() {
        return RltPermission.read_write.equals(permission);
    }

    public Long getMaxAgeMillis(){
        if(maxAge==null)
            return null;

        return (maxAge + 3) * 1000L;

    }
}


