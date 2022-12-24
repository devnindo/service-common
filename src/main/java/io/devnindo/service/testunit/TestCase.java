package io.devnindo.service.testunit;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizUser;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

import static io.devnindo.service.testunit.TestCaseFlow.*;
public class TestCase implements
          DataIF,  UserIF, AssertRuleIF
{

    private BizAction bizAction;
    private BizUser bizUser;
    private JsonObject jsData;

    private CountDownLatch countDownLatch;


    private TestCase(BizAction bizAction$)
    {
        bizAction = bizAction$;
        countDownLatch = new CountDownLatch(1);
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
    public void assertRule(BiConsumer<BizResponse, BizException> assertRule$) {
        BizRequest request = new BizRequest(null, null, null, bizUser, jsData);
        Single<BizResponse> bizResponseSingle =  bizAction.executeOn(request);
        AssertionError[] assertionError = {null};
        bizResponseSingle.doOnSuccess(response -> {
                    try{
                        assertRule$.accept(response, null);
                    }catch (AssertionError err){
                        assertionError[0] = err;
                    }finally {
                        countDownLatch.countDown();
                    }
                })
                .doOnError(error -> {
                    if(error instanceof BizException)
                    {
                        try{
                            assertRule$.accept(BizResponse.INTERNAL_ERROR, (BizException) error);
                        }catch (AssertionError err){
                            assertionError[0] = err;
                        }finally {
                            countDownLatch.countDown();
                        }
                    }
                }).subscribe();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(assertionError[0]!=null)
            throw assertionError[0];
    }
}
