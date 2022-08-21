package io.devnindo.service.auth;

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
        public IChannelClient channel(String channelId);
    }

    public interface IChannelClient
    {
        public IBuild channelClient(String clientId$);
    }

    public interface IBuild
    {
        public IBuild signedDatime(Instant signedDatime$);
        public BizUser build();
    }
}
