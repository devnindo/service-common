package io.devnindo.service.auth;

import io.devnindo.service.exec.action.request.BizAccessInfo;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.util.Either;
import io.devnindo.service.util.Values;
import io.devnindo.datatype.validation.Violation;
import io.reactivex.Single;
import io.devnindo.datatype.json.JsonObject;

import javax.inject.Inject;

public class JWTSessionHandler extends BizSessionHandler
{

    private final JwtHandlerIF jwtHandler;
    private static final Violation VALID_SESSION_VIOLATION = Violation.withCtx("SESSION_VALIDATION_CONSTRAINT", "VALID_SESSION");

    @Inject
    public JWTSessionHandler(JWTConfig sessionConfig$)
    {
        jwtHandler = new DefaultJwtHandler(sessionConfig$);
    }

    @Override
    public Single<Either<Violation, BizUser>> doBiz(BizAccessInfo input$) {
        String _accessToken = input$.getAccessToken();

        if (Values.NOT_AVAILABLE.equals(_accessToken))
            return Single.just(Either.right(BizUser.globalAnonymous()));

        boolean bearerToken = _accessToken.startsWith("Bearer ");
        String token = _accessToken;
        if(bearerToken)
            token = _accessToken.substring("Bearer ".length());

        Either<Violation, JsonObject> jwtEither = jwtHandler.validateJWT(token);

        if(jwtEither.isLeft())
            return Single.just(Either.left(jwtEither.left()));
        else
        {
            Either<Violation, BizUser> bizUserEither = BeanSchema.of(BizUser.class).apply(jwtEither.right());
            if(bizUserEither.isLeft())
                return Single.just(Either.left(VALID_SESSION_VIOLATION));
            else return Single.just(Either.right(bizUserEither.right()));
        }
    }


    /*@Override
    public Single<Either<Violation, BizUser>> executeOn(BizAccessInfo accessInfo) {

    }*/
}
