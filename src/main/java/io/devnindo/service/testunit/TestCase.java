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
package io.devnindo.service.testunit;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizUser;
import io.devnindo.service.util.ThreadUtil;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    public void applyRule(BiConsumer<BizResponse, BizException> rule$) {
        BizRequest request = new BizRequest(null, null, null, bizUser, jsData);
        Single<BizResponse> bizResponseSingle =  bizAction.executeOn(request);
        AssertionError[] assertionError = {null};
        bizResponseSingle.doOnSuccess(response -> {
                    try{
                        ThreadUtil.logCurrent(jsData.getString("name")+"--OnSuccess");
                        rule$.accept(response, null);
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
                            rule$.accept(BizResponse.INTERNAL_ERROR, (BizException) error);
                        }catch (AssertionError err){
                            assertionError[0] = err;
                        }finally {
                            countDownLatch.countDown();
                        }
                    }
                }).subscribe();

        try {
            ThreadUtil.logCurrent(jsData.getString("name")+"--ApplyRule");
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(assertionError[0]!=null)
            throw assertionError[0];
    }
}
