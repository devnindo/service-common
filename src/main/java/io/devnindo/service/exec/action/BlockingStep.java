package io.devnindo.service.exec.action;

import io.devnindo.service.util.CompositionUtil;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.reactivex.Single;

public abstract class  BlockingStep<IP, OP> extends BizStep<IP, OP>
{
    protected abstract   Either<Violation, OP> doBlocking(IP ip$);

    @Override
    protected final Single<Either<Violation, OP>> doBiz(IP input$)
    {

        Single<Either<Violation, OP>> bizSingle =
                Single.create
                (
                        src ->
                        {
                            try {
                                Either<Violation, OP> either = doBlocking(input$);
                                src.onSuccess(either);
                            }catch (Exception excp)
                            {
                                if(excp instanceof BizException)
                                    src.onError(excp);
                                else src.onError(BizException.onException(name(), excp));
                            }
                        }
                );

        return CompositionUtil.composeVertexScheduler(bizSingle)
                // here only possible exception would be RuntimeException due to null-vertx-context
                .onErrorResumeNext(error -> Single.error(this.$bizException(error)));

    }


}
