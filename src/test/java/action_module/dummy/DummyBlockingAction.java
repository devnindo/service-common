package action_module.dummy;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanValidator;
import io.devnindo.service.exec.action.BlockingBizAction;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.service.exec.auth.BizAuth;
import io.devnindo.service.exec.auth.BizUser;

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

        System.out.println("Action Dummy: "+reqData$.getName());
        try {
            Thread.sleep(reqData$.getSleepingTime());
            System.out.println(Thread.currentThread().getName());
        } catch (InterruptedException e) {

        }
        return BizResponse.success(new JsonObject().put("response", 123));
    }
}
