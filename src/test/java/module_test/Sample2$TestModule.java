package module_test;

import io.devnindo.service.testunit.BizTestModule;
import io.devnindo.service.testunit.module_example.dummy.action.DummyBlockingAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Sample2$TestModule extends BizTestModule {



    @Test
    public void test_1(){
        forAction(DummyBlockingAction.class)
                .withUser(UserForTest.seniorRashed())
                .withData(DataForTest.module_2_data_1())
                .applyRule((bizResponse, bizException) -> {
                    assertEquals(Boolean.TRUE, bizResponse.isSuccess());
                });
    }

    @Test
    public void test_2(){
        forAction(DummyBlockingAction.class)
                .withUser(UserForTest.seniorRashed())
                .withData(DataForTest.module_2_data_2())
                .applyRule((bizResponse, bizException) -> {
                    assertEquals(Boolean.TRUE, bizResponse.isSuccess());
                });
    }
}
