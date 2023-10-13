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

public final class BizUserIF
{
    public interface IUserId{
        public IPrefName userId(Long userId$);
    }

    public interface IPrefName{
        public IRole prefName(String prefName$);
    }

    public interface IRole
    {
        public IDomain role(String role$);
    }

    public interface IDomain
    {
        public IChannel domain(String domainId);
    }

    public interface IChannel
    {
        public IBuild channel(String channelId);
    }

   /* public interface IChannelClient
    {
        public IBuild channelClient(String clientId$);
    }*/

    public interface IBuild
    {
        public BizUser build();
    }
}
