import io.devnindo.service.deploy.components.BizComponent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiTestModule
{
    private BizComponent component ;

    private final <T extends BizComponent> T bizComponent(){
        return (T) component;
    }

    @BeforeAll
    public  static void init(){
        System.out.println("# Before All Executed");
    }

    @AfterAll
    public static void finish(){
        System.out.println("# After All Executed");
    }
}
