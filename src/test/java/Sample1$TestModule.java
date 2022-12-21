import io.devnindo.service.testunit.ServiceTestModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class Sample1$TestModule extends ServiceTestModule
{


    @Test
    public void test_1(){

        initSimpleCase().execute(3000, ()->{
            Assertions.assertEquals(Boolean.TRUE, Boolean.TRUE);
        });
    }

    @Test
    public void test_2(){
        initSimpleCase().execute(4000, ()->{
            Assertions.assertEquals(Boolean.TRUE, Boolean.FALSE);
        });
    }
}
