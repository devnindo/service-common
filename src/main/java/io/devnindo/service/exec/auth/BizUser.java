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
package io.devnindo.service.exec.auth;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.util.Values;

import java.time.Instant;

public class BizUser implements DataBean
{
    Long userId;

    String prefName;

    Instant createdDatime;

    BizUserStatus status;

    String role; //schema -> {channel_id: role_name_id}

    /*
     *  channelId: channel bizUser trying to access with this token
     * */
    String channelId;

    /**
     *  domain under which its accessing channel exposes services for user
     *  for example: elearning-app is the domain of channel lojens
     * */
    String domainId;


    /**
     * Referenced Anonymous User is an user who is
     * unregistered but still referenced by a valid ref-client
     * Genarally auth-service create this on login request
     * by client.
     *
     * Global Anonymous User is an user who is unregistered
     * and could not be referenced from a valid ref-client
     *
     * */


    public Boolean isRefAnonymous()
    {
        return BizUserStatus.r_anonymous.equals(status);
    }

    public Boolean isGlobalAnonymous()
    {
        return BizUserStatus.g_anonymous.equals(status);
    }

    public Boolean isRegisteredUser()
    {
        return BizUserStatus.reg_user.equals(status);
    }

    public String getChannelId() {
        return channelId;
    }

    public BizUserStatus getStatus() {
        return status;
    }

    public Instant getCreatedDatime() {
        return createdDatime;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPrefName(){ return prefName; }

    public String getRole() {
        return role;
    }


    public String getDomainId() {
        return domainId;
    }





    public static final BizUser referencedAnonymous(String refId$, String channelId$,  String domainId$)
    {
        BizUser _anonymous = new  BizUser();

        _anonymous.userId = -1L;
        _anonymous.prefName = "ANONYMOUS "+ refId$;
        _anonymous.status = BizUserStatus.r_anonymous;
        _anonymous.role = Values.NOT_APPLICABLE;
        _anonymous.channelId = channelId$;
        _anonymous.domainId = domainId$;

        return  _anonymous;
    }

    public static final BizUser globalAnonymous( )
    {
        BizUser _anonymous = new  BizUser();

        _anonymous.userId = -2L;
        _anonymous.status = BizUserStatus.g_anonymous;
        _anonymous.prefName = "GAnonymous";
        _anonymous.role = Values.NOT_APPLICABLE;
        _anonymous.channelId = Values.NOT_AVAILABLE;
        _anonymous.domainId = Values.NOT_AVAILABLE;

        return  _anonymous;
    }


}
