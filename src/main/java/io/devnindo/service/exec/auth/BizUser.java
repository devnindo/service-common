package io.devnindo.service.exec.auth;

import io.devnindo.service.util.Values;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;

import java.time.Instant;

public class BizUser implements DataBean
{
    @AField
    Long userId;

    @AField
    String prefName;

    @AField
    Instant signedDatime;

    @AField
    BizUserStatus status;

    @AField
    String role; //schema -> {channel_id: role_name_id}

    /*
     *  channelId: channel bizUser trying to access with this token
     * */
    @AField
    String channelId;

    /**
     *  domain under which its accessing channel exposes services for user
     *  for example: elearning-app is the domain of channel lojens
     * */
    @AField
    String domainId;

    /**
     *  clientId: src client through which user initiated the access
     * */
    @AField
    String channelClientId;

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

    public Instant getSignedDatime() {
        return signedDatime;
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


    public String getChannelClientId() {
        return channelClientId;
    }



    public static final BizUser referencedAnonymous(String channelId$, String channelClientId$, String domainId$)
    {
        BizUser _anonymous = new  BizUser();

        _anonymous.userId = -1L;
        _anonymous.prefName = "RAnonymous";
        _anonymous.status = BizUserStatus.r_anonymous;
        _anonymous.role = Values.NOT_APPLICABLE;
        _anonymous.channelId = channelId$;
        _anonymous.domainId = domainId$;
        _anonymous.channelClientId = channelClientId$;

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
        _anonymous.channelClientId = Values.NOT_AVAILABLE;

        return  _anonymous;
    }


}

