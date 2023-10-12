package io.devnindo.service.exec.auth;

import java.time.Instant;
import static io.devnindo.service.exec.auth.BizUserIF.*;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
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
