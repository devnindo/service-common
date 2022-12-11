package io.devnindo.service.testunit;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizUser;

import java.util.Map;

import static io.devnindo.service.testunit.TestCaseFlow.*;
public class TestCase implements
         ExecuteIF,  DataIF,  UserIF
{

    private BizAction bizAction;
    private BizUser bizUser;
    private JsonObject jsData;
    protected TestCase(BizAction bizAction$)
    {
         bizAction = bizAction$;
    }

    @Override
    public DataIF withUser(BizUser bizUser$) {
        bizUser = bizUser$;
        return this;
    }

    @Override
    public ExecuteIF withData(JsonObject jsData$) {
        jsData = jsData$;
        return this;
    }

    @Override
    public void execute() {
        // we are only interested to test with bizUser and jsonData
        BizRequest request = new BizRequest(null, null, null, bizUser, jsData);
        bizAction.executeOn(request);
    }
}
