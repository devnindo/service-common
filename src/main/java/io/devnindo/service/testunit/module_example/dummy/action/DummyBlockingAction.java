package io.devnindo.service.testunit.module_example.dummy.action;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanValidator;
import io.devnindo.service.exec.action.BlockingBizAction;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizAuth;
import io.devnindo.service.exec.auth.BizUser;
import io.devnindo.service.testunit.module_example.dummy.DummyData;
import io.devnindo.service.util.ThreadUtil;

import javax.inject.Inject;
import javax.inject.Named;

public class DummyBlockingAction extends BlockingBizAction<DummyData>
{
    @Inject
    public DummyBlockingAction(
            @Named("DUMMY_VLD")
            BeanValidator<DummyData> validator$,
            @Named("DUMMY_SENIOR")
            BizAuth bizAuth$)
    {
        super(validator$, bizAuth$);
    }

    @Override
    protected BizResponse doBlockingBiz(DummyData reqData$, BizUser bizUser$) {

        ThreadUtil.logCurrent(reqData$.getName()+"--ACTION");
        ThreadUtil.sleepingBlock(reqData$.getSleepingTime());
        return BizResponse.success(new JsonObject().put("reply", reqData$.getReply()));
    }
}
