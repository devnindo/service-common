package io.devnindo.service.deploy.test;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizUser;
import io.reactivex.rxjava3.core.Single;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;

/**
 *  An util class to faciliate action test
 * */

@Singleton
public class TestExecutor
{
    private final Map<Class<? extends BizAction>, Provider<BizAction>> actionMap;
    private final BizUser bizUser;

    @Inject
    public TestExecutor(
            Map<Class<? extends BizAction>, Provider<BizAction>> actionMap$,
            BizUser bizUser$)
    {
        actionMap = actionMap$;
        bizUser = bizUser$;
    }

    public Single<BizResponse> test(Class<? extends BizAction> actionClz$, JsonObject data$){
        if(actionMap.containsKey(actionClz$))
        {
            BizAction bizAction = actionMap.get(actionClz$).get();
            //first 3 field not needed for simple test;
            BizRequest request = new BizRequest(null, null, null, bizUser, data$);
            return bizAction.executeOn(request);
        }
        else return Single.just(BizResponse.ACTION_NOT_FOUND);
    }
}
