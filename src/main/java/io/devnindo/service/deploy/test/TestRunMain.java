package io.devnindo.service.deploy.test;

import io.devnindo.service.BizMain;
import io.devnindo.service.configmodels.RuntimeMode;

import java.io.IOException;

public class TestRunMain
{
    public static void main(String... args) throws IOException, IllegalAccessException {
        ActionTestExecutor testExec = BizMain.initTestExec(RuntimeMode.DEV);


    }
}
