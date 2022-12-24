package io.devnindo.service.testunit;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.action.BizException;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizUser;

import java.util.function.BiConsumer;

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
        AssertRuleIF withData(JsonObject data);
    }

    public static interface AssertRuleIF{
        void assertRule(BiConsumer<BizResponse, BizException> assertRule$);
    }


}
