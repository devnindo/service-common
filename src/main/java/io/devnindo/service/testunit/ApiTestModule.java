package io.devnindo.service.testunit;

import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.deploy.components.BizComponent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ApiTestModule
{
    private ActionComponent component ;

    public abstract RuntimeMode runtimeMode();// return RuntimeMode

    private final <T extends BizComponent> T actionComponent(){
        return (T) component;
    }

    @BeforeAll
    public    void init(){
        System.out.println("# Before All Executed");
    }

    @AfterAll
    public   void finish(){
        System.out.println("# After All Executed");
    }
}
