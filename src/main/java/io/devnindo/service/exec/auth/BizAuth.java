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

import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;

import java.util.Objects;

public interface BizAuth
{
    Violation USER_ACCESS_VIOLATION = Violation.withCtx("USER_ACCESS_AUTH", "VALID_ROLE");
    Violation REFERENCE_ACCESS_VIOLATION = Violation.withCtx("REF_ACCESS_AUTH", "VALID_REFERENCE");

    Violation REGISTERED_ACCESS_VIOLATION = Violation.withCtx("REG_USER_AUTH", "REGISTRATION");

    public Either<Violation, Void> checkAccess(BizUser bizUser$);

    /**
     *  NO AUTH is for global access, it allows access unconditionally
     * */
    BizAuth NO_AUTH = bizRequest$ -> Either.right(null);
    /**
     *  REFERENCE_ACCESS_AUTH allow access to any registered and referenced anonymous user
     * */
    BizAuth REFERENCE_ACCESS_AUTH = bizUser$ -> {
        // ref-anonymous allowed then any user with a role will also be allowed
        if(bizUser$.isRefAnonymous() || bizUser$.isRegisteredUser())
            return Either.right(null);
        else return Either.left(REFERENCE_ACCESS_VIOLATION);

    };

    BizAuth REGISTERED_ACCESS_AUTH = bizUser$ -> {
        // ref-anonymous allowed then any user with a role will also be allowed
        if(bizUser$.isRegisteredUser())
            return Either.right(null);
        else return Either.left(REGISTERED_ACCESS_VIOLATION);

    };


    public static BizAuth forRole(String... roles$)
    {
        Objects.requireNonNull(roles$);
        return bizUser$ -> {

            if(bizUser$.isRefAnonymous() || bizUser$.isGlobalAnonymous())
                return Either.left(REGISTERED_ACCESS_VIOLATION);

            String userRole = bizUser$.role;
            for(String r : roles$){
                if(r.equals(userRole))
                    return Either.right(null);
            }

            return Either.left(USER_ACCESS_VIOLATION);

        };
    }






}
