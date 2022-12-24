package module_test;

import io.devnindo.service.testunit.ServiceTestModule;
import io.devnindo.service.testunit.module_example.dummy.action.DummyBlockingAction;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class Sample1$TestModule extends ServiceTestModule
{


    @Test
    public void test_1(){
        newCaseFor(DummyBlockingAction.class)
            .withUser(UserForTest.seniorRashed())
            .withData(DataForTest.dummyData_1())
            .assertRule((bizResponse, bizException) -> {
                assertEquals(Boolean.TRUE, bizResponse.isSuccess());
            });

    }

    @Test
    public void test_2(){
        newCaseFor(DummyBlockingAction.class)
                .withUser(UserForTest.seniorRashed())
                .withData(DataForTest.dummyData_2())
                .assertRule((bizResponse, bizException) -> {
                    assertEquals(Boolean.TRUE, bizResponse.isSuccess());
                });
    }
}
