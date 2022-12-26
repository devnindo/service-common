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

    private final <T extends ActionComponent> T actionComponent(){
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

    @BeforeEach
    void logActionInstance(){
        System.out.println("#ACTION COMPONENT: "+actionComponent().hashCode());
    }

    protected TestCaseFlow.UserIF forAction(Class<? extends BizAction> actionClz){
        BizAction bizAction = actionComponent.actionMap().get(actionClz).get();
        return TestCase.init(bizAction);
    }

    @AfterAll
    void finish(){

    }

}
