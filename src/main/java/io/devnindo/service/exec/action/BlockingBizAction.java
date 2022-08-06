package io.devnindo.service.exec.action;

import io.devnindo.service.exec.action.response.BizResponse;

import io.devnindo.service.exec.auth.BizAuth;
import io.devnindo.service.exec.auth.BizUser;
import io.devnindo.service.util.CompositionUtil;
import io.devnindo.datatype.schema.BeanValidator;
import io.devnindo.datatype.schema.DataBean;
import io.reactivex.Single;

public abstract class BlockingBizAction<T extends DataBean> extends BizAction<T>
{


    public BlockingBizAction(BeanValidator<T> validator$, BizAuth bizAuth$)
    {
        super(validator$, bizAuth$);

    }



    protected abstract BizResponse doBlockingBiz(T reqData$, BizUser bizUser$);

    protected final Single<BizResponse> doBiz(T reqData$, BizUser bizUser$)
    {
        Single<BizResponse> singleResponse = Single.create(src -> {
            try{
                BizResponse response = doBlockingBiz(reqData$, bizUser$);
                src.onSuccess(response);
            }catch (Throwable exception)
            {
                src.onError(exception);
            }
        });

        return CompositionUtil.composeVertexScheduler(singleResponse) ;

    }



}
