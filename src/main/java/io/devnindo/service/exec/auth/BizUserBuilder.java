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

import java.time.Instant;
import static io.devnindo.service.exec.auth.BizUserIF.*;

public final class BizUserBuilder
    implements IBuild, IUserId, IPrefName, IRole, IDomain, IChannel
{

    private final BizUser bizUser;

    protected BizUserBuilder()
    {
        bizUser = new BizUser();
        bizUser.status = BizUserStatus.reg_user;
    }

    public static final IUserId init()
    {
        return new BizUserBuilder();
    }


    @Override
    public  IPrefName userId(Long userId$) {

        bizUser.userId = userId$;
        return this;
    }


    @Override
    public  IRole prefName(String prefName$) {
        bizUser.prefName = prefName$;
        return this;
    }


    @Override
    public  IDomain role(String role$) {
        bizUser.role = role$;
        return this;
    }

    @Override
    public  IChannel domain(String domainId$) {
        bizUser.domainId = domainId$;
        return this;
    }

    @Override
    public  IBuild channel(String channelId$) {
        bizUser.channelId = channelId$;
        return this;
    }


    @Override
    public BizUser build() {
        bizUser.createdDatime = Instant.now();
        return bizUser;
    }
}
