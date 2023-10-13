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
import io.devnindo.datatype.util.ClzUtil;
import io.devnindo.service.deploy.BizComponentProvider;
import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.deploy.components.BizComponent;
import io.devnindo.service.exec.RxScheduler;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.util.ServiceConfigUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BizTestModule
{
    private static ActionComponent actionComponent ;

    protected final <T extends ActionComponent> T actionComponent(){
        return (T) actionComponent;
    }

    @BeforeAll
     void init() throws IllegalAccessException, IOException {
        /**
         *  to make sure single actionComponent instance in case
         *  multiple test module concurrent initialization
         * */
         if(actionComponent == null)
         {
             synchronized (ActionComponent.class){
                 if(actionComponent == null)
                 {
                     JsonObject runtimeConfig = ServiceConfigUtil.readRuntimeConfig(RuntimeMode.dev);
                     BaseTestDependency testDependency = new BaseTestDependency();
                     BizComponentProvider provider = ClzUtil.findClzAndReflect(BizComponentProvider.class);
                     RxScheduler.init(testDependency.blockingScheduler(), testDependency.asyncScheduler());
                     actionComponent = provider.actionComponent(runtimeConfig, testDependency);
                 }
             }
         }

    }

    protected TestCaseFlow.UserIF forAction(Class<? extends BizAction> actionClz){
        BizAction bizAction = actionComponent.actionMap().get(actionClz).get();
        return TestCase.init(bizAction);
    }

    @AfterAll
    void finish(){

    }

}
