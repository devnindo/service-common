import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.testunit.ApiTestModule;
import org.junit.jupiter.api.Test;

public class Sample2$TestModule extends ApiTestModule {

    @Override
    public RuntimeMode runtimeMode() {
        return RuntimeMode.dev;
    }

    @Test
    public void test_1(){
        System.out.println("test_1 of module: "+getClass().getName());
    }

    @Test
    public void test_2(){
        System.out.println("test_2 of module: "+getClass().getName());
    }
}
