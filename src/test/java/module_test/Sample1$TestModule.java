package module_test;

import io.devnindo.service.testunit.BizTestModule;
import io.devnindo.service.testunit.module_example.dummy.action.DummyBlockingAction;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class Sample1$TestModule extends BizTestModule
{


    @Test
    public void test_1(){
        forAction(DummyBlockingAction.class)
            .withUser(UserForTest.seniorRashed())
            .withData(DataForTest.module_1_data_1())
            .applyRule((bizResponse, bizException) -> {
                assertEquals(Boolean.TRUE, bizResponse.isSuccess());
            });

    }

    @Test
    public void test_2(){
        forAction(DummyBlockingAction.class)
            .withUser(UserForTest.seniorRashed())
            .withData(DataForTest.module_1_data_2())
            .applyRule((bizResponse, bizException) -> {
                assertEquals(Boolean.TRUE, bizResponse.isSuccess());
            });
    }
}
