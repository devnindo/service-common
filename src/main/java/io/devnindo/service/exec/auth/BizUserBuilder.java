package io.devnindo.service.exec.auth;

import java.time.Instant;

public final class BizUserBuilder
    implements BizUserIF.IBuild, BizUserIF.IUserId, BizUserIF.IPrefName, BizUserIF.IRole, BizUserIF.IDomain, BizUserIF.IChannel, BizUserIF.IChannelClient
{

    private final BizUser bizUser;

    protected BizUserBuilder()
    {
        bizUser = new BizUser();
        bizUser.status = BizUserStatus.reg_user;
    }

    public static final BizUserBuilder init()
    {
        return new BizUserBuilder();
    }


    @Override
    public BizUserIF.IPrefName userId(Long userId$) {

        bizUser.userId = userId$;
        return this;
    }




    @Override
    public BizUserIF.IRole prefName(String prefName$) {
        bizUser.prefName = prefName$;
        return this;
    }


    @Override
    public BizUserIF.IDomain role(String role$) {
        bizUser.role = role$;
        return this;
    }

    @Override
    public BizUserIF.IChannel domain(String domainId$) {
        bizUser.domainId = domainId$;
        return this;
    }

    @Override
    public BizUserIF.IChannelClient channel(String channelId$) {
        bizUser.channelId = channelId$;
        return this;
    }

    @Override
    public BizUserIF.IBuild channelClient(String clientId$) {
        bizUser.channelClientId = clientId$;
        return this;
    }

    @Override
    public BizUserIF.IBuild signedDatime(Instant signedDatime$) {
        bizUser.signedDatime = signedDatime$;
        return this;
    }

    @Override
    public BizUser build() {
        return bizUser;
    }
}
