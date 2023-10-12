/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.exec;


import io.devnindo.service.configmodels.ParamService;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.request.BizAccessInfo;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizSessionHandler;
import io.devnindo.service.util.DeployParams;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.json.JsonObject;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.Vertx;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
@Singleton
public class BizExecutor {

    //private final BizSessionRepo sessionRepo;
    public final String serviceID;
    public final BizSessionHandler sessionHandler;
    private final Vertx vertx;
    private final BizManagerApi managerApi;
    private final Map<BizAction.Identity, Provider<BizAction>> actionMap;


    @Inject
    public BizExecutor(
            @Named(DeployParams.SERVICE_ID) String serviceID$,
            BizSessionHandler sessionHandler$,
            Map<Class<? extends BizAction>, Provider<BizAction>> actionMap$,
            Vertx vertex$,
            BizManagerApi managerApi$
    )
    {
        serviceID = serviceID$;
        sessionHandler = sessionHandler$;
        vertx = vertex$;
        managerApi = managerApi$;

        actionMap = new HashMap<>(actionMap$.size());
        actionMap$.entrySet().forEach( entry -> {
            BizAction.Identity _indentity = BizAction.Identity.createFrom(entry.getKey()).right();
            actionMap.put(_indentity, entry.getValue());
        });

    }


    public Set<BizAction.Identity> identitySet() {
        return  actionMap.keySet();
    }



    public final Single<BizResponse> executeOn(BizRequest request)
    {
        Provider<BizAction> actionProvider = actionMap.get(request.actionIdentity);

        BizAction<?> action = actionProvider.get();
        return action.executeOn(request)
                // .doOnSuccess(response -> this.logResponse(request, response))
                .onErrorReturn(error$ -> {
                        BizException bizException = BizException.class.cast(error$);
                        return BizResponse.error(bizException.toJson());
                });
    }

    Single<BizResponse> executeOn(JsonObject msgObj$)
    {

        // JsonObject msgObj = (JsonObject) message.body();
        JsonObject accessData = msgObj$.getJsonObject(ParamService.ACCESS_INFO);
        String actionID = msgObj$.getString(ParamService.ACTION_ID);
        JsonObject reqData = msgObj$.getJsonObject(ParamService.REQUEST_DATA);

        Either<String, BizAction.Identity> identityEither = BizAction.Identity.createFrom(actionID);
        if(identityEither.isLeft())
            return Single.just(BizResponse.BAD_REQUEST);

        BizAction.Identity identity = identityEither.right();
        if(actionMap.containsKey(identityEither.right()) == false)
            return Single.just(BizResponse.ACTION_NOT_FOUND);

        Either<Violation, BizAccessInfo> accessInfoEither = accessData.toBeanEither(BizAccessInfo.class);
        if(accessInfoEither.isLeft())
            return Single.just(BizResponse.BAD_REQUEST);

        BizAccessInfo accessInfo = accessInfoEither.right();
        Single<BizRequest> reqSingle =
                sessionHandler.executeOn(accessInfoEither.right())
                        .flatMap(
                                bizUser ->
                                {
                                    BizRequest request = new BizRequest(serviceID, identity, accessInfo.getClientInfo(), bizUser, reqData);
                                    return Single.just(request);

                                }
                        );

        return reqSingle.flatMap(this::executeOn);
    }



}

