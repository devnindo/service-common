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
import org.junit.jupiter.api.TestInstance;

import javax.inject.Provider;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ServiceTestModule
{
    private static ActionComponent actionComponent ;

    private final <T extends BizComponent> T actionComponent(){
        return (T) actionComponent;
    }

    @BeforeAll
     void init() throws IllegalAccessException, IOException {
         if(actionComponent == null){
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


    protected TestCaseFlow.UserIF newCaseFor(Class<? extends BizAction> actionClz){
        BizAction bizAction = actionComponent.actionMap().get(actionClz).get();
        return TestCase.init(bizAction);
    }

    @AfterAll
    void finish(){

    }

}
