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
    Permission permission;

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

    public Integer getMaxAge() {
        return maxAge;
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


