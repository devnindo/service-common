package io.devnindo.service.realtime;

import io.devnindo.datatype.schema.DataBean;

import java.security.Permission;
import java.time.Instant;

public class RltTopic implements DataBean
{
    Long userId;
    String topicId;
    Long maxAge; // in seconds

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

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public boolean canUserWrite() {
        return RltPermission.read_write.equals(permission);
    }


    public  String getRltId(){
        return userId+"-"+topicId;
    }
}


