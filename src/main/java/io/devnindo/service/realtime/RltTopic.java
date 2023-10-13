/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    Instant expireDatime; // in seconds
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

    public Instant getExpireDatime() {
        return expireDatime;
    }

    public RltTopic setExpireDatime(Instant expireDatime) {
        this.expireDatime = expireDatime;
        return this;
    }

    public boolean shouldExpire(){
        return Instant.now().isAfter(expireDatime);
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


}
