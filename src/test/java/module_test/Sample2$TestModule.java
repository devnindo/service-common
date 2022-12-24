package module_test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class Sample2$TestModule{



    @Test
    public void test_1(){
        System.out.println("test_1 of module: "+getClass().getName());
    }

    @Test
    public void test_2(){
        System.out.println("test_2 of module: "+getClass().getName());
    }
}
