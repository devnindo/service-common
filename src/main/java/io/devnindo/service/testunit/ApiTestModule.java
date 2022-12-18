package io.devnindo.service.testunit;

import io.devnindo.service.BizMain;
import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.deploy.components.BizComponent;
import io.devnindo.service.exec.action.BizAction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ApiTestModule
{
    private ActionComponent actionComponent ;
    private CountDownLatch blockingLatch;
    public abstract RuntimeMode runtimeMode();// return RuntimeMode

    private final <T extends BizComponent> T actionComponent(){
        return (T) actionComponent;
    }

    @BeforeAll
    private void init(){
        try {
            actionComponent =  //BizMain.init(runtimeMode()).actionComponent();


        } catch (IllegalAccessException | IOException exp) {
            throw new RuntimeException(exp);
        }
    }



    protected TestCaseFlow.UserIF newCaseFor(Class<BizAction> actionClz){
        BizAction bizAction = actionComponent.actionMap().get(actionClz).get();
        return TestCase.init(bizAction);
    }

    @AfterAll
    private void finish(){
        System.out.println("# After All Executed");
    }

}
