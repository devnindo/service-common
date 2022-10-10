package io.devnindo.service.deploy.test;

import io.devnindo.service.BizMain;
import io.devnindo.service.configmodels.RuntimeMode;

public class TestRunMain
{
    public static void main(String... args)
    {
        ActionTestExecutor testExec = BizMain.initTestExec(RuntimeMode.DEV);


    }
}
