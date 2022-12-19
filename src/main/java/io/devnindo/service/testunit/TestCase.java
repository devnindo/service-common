package io.devnindo.service.testunit;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizUser;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

import java.util.function.BiConsumer;

import static io.devnindo.service.testunit.TestCaseFlow.*;
public class TestCase implements
         ExecuteIF,  DataIF,  UserIF, AssertRuleIF
{

    private BizAction bizAction;
    private BizUser bizUser;
    private JsonObject jsData;

    BiConsumer<BizResponse, BizException> assertRule;

    private TestCase(BizAction bizAction$){
        bizAction = bizAction$;
    }
    protected static final UserIF init(BizAction bizAction$)
    {
        return new TestCase(bizAction$);
    }

    @Override
    public DataIF withUser(BizUser bizUser$) {
        bizUser = bizUser$;
        return this;
    }

    @Override
    public AssertRuleIF withData(JsonObject jsData$) {
        jsData = jsData$;
        return this;
    }

    @Override
    public ExecuteIF andRule(BiConsumer<BizResponse, BizException> assertRule$) {
        assertRule = assertRule$;
        return this;
    }

    @Override
    public void execute() {
        // we are only interested to test with bizUser and jsonData
        BizRequest request = new BizRequest(null, null, null, bizUser, jsData);
        Single<BizResponse> bizResponseSingle =  bizAction.executeOn(request);
        AssertionError[] assertionError = {};
        bizResponseSingle.doOnSuccess(response -> {
                    try{
                        assertRule.accept(response, null);
                    }catch (AssertionError err){
                        assertionError[0] = err;
                    }
        })
        .doOnError(error -> {
            if(error instanceof BizException)
            {
                try{
                    assertRule.accept(BizResponse.INTERNAL_ERROR, (BizException) error);
                }catch (AssertionError err){
                    assertionError[0] = err;
                }
            }
        }) ;
    }


}
