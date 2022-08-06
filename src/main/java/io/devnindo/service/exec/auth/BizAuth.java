package io.devnindo.service.exec.auth;

import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;

import java.util.Objects;

public interface BizAuth
{
    Violation SINGLE_CHANNEL_ACCESS_AUTH_VIOLATION = Violation.withCtx("SINGLE_CHANNEL_ACCESS_AUTH", "VALID_ROLE");
    Violation REFERENCE_ACCESS_AUTH_VIOLATION = Violation.withCtx("REF_ACCESS_AUTH", "VALID_REFERENCE");


    public Either<Violation, Void> checkAccess(BizRequest bizRequest$);

    /**
     *  NO AUTH is for global access, it always access unconditionally
     * */
    BizAuth NO_AUTH = bizRequest$ -> Either.right(null);
    /**
     *  REFERENCE_ACCESS_AUTH allow access to any registered and referenced anonymous user
     * */
    BizAuth REFERENCE_ACCESS_AUTH = bizRequest$ -> {
        // ref-anonymous allowed then any user with a role will also be allowed
        if(bizRequest$.bizUser.isRefAnonymous() || bizRequest$.bizUser.isRegisteredUser())
            return Either.right(null);
        else return Either.left(REFERENCE_ACCESS_AUTH_VIOLATION);

    };

    BizAuth ALL_USER_ACCESS_AUTH = bizRequest$ -> {
        // ref-anonymous allowed then any user with a role will also be allowed
        if(bizRequest$.bizUser.isRegisteredUser())
            return Either.right(null);
        else return Either.left(REFERENCE_ACCESS_AUTH_VIOLATION);

    };

    /**
     * SingleChannelAuth: functional definition to allow access an user with a role
     * to a domain service having one-to-one mapping to domain channel
     * */
    public static BizAuth singleChannelAuth(String... roles$)
    {
        Objects.requireNonNull(roles$);
        return bizRequest$ -> {

            if(bizRequest$.bizUser.isRefAnonymous() || bizRequest$.bizUser.isGlobalAnonymous())
                return Either.left(SINGLE_CHANNEL_ACCESS_AUTH_VIOLATION);

            String userRole = bizRequest$.bizUser.role;
            for(String r : roles$){
                if(r.equals(userRole))
                    return Either.right(null);
            }

            return Either.left(SINGLE_CHANNEL_ACCESS_AUTH_VIOLATION);

        };
    }






}
