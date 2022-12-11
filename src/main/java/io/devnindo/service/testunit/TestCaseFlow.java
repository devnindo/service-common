package io.devnindo.service.testunit;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.auth.BizUser;

public final class TestCaseFlow
{
    private TestCaseFlow(){}

    public static interface ActionIF
    {
        UserIF forAction(BizAction bizAction);
    }

    public static interface UserIF{
        DataIF withUser(BizUser bizUser);
    }

    public static interface DataIF{
        ExecuteIF withData(JsonObject data);
    }

    public static interface ExecuteIF{
        void execute();
    }

}
