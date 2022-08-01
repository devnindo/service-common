package io.devnindo.service.exec.auth;

import java.time.Instant;

import static io.devnindo.service.exec.auth.BizUserIF.*;
public final class BizUserBuilder
    implements  IBuild, IUserId, IPrefName,  IRole, IDomain, IChannel, IChannelClient
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
    public IPrefName userId(Long userId$) {

        bizUser.userId = userId$;
        return this;
    }




    @Override
    public IRole prefName(String prefName$) {
        bizUser.prefName = prefName$;
        return this;
    }


    @Override
    public IDomain role(String role$) {
        bizUser.role = role$;
        return this;
    }

    @Override
    public IChannel domain(String domainId$) {
        bizUser.domainId = domainId$;
        return this;
    }

    @Override
    public IChannelClient channel(String channelId$) {
        bizUser.channelId = channelId$;
        return this;
    }

    @Override
    public IBuild channelClient(String clientId$) {
        bizUser.channelClientId = clientId$;
        return this;
    }

    @Override
    public IBuild signedDatime(Instant signedDatime$) {
        bizUser.signedDatime = signedDatime$;
        return this;
    }

    @Override
    public BizUser build() {
        return bizUser;
    }
}
