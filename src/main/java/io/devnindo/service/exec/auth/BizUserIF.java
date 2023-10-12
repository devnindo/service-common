package io.devnindo.service.exec.auth;

import java.time.Instant;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
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
