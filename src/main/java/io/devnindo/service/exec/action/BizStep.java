package io.devnindo.service.exec.action;

import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.reactivex.Single;

import java.util.function.Function;

public abstract class BizStep<IP, OP> {


    public static  <IP, OP> BizStep<IP, OP> rx(String name$, Function<IP, Single<Either<Violation, OP>>> bizImpl$)
    {
        return new BizStep<IP, OP>() {
            @Override
            public String name() {
                return name$;
            }

            @Override
            public Single<Either<Violation, OP>> doBiz(IP input$) {
                return bizImpl$.apply(input$);
            }
        };
    }

    public static  <IP, OP> BizStep<IP, OP> blocking(String name$, Function<IP,  Either<Violation, OP>> bizImpl$)
    {
        return new BlockingStep<IP, OP>() {
            @Override
            public Either<Violation, OP> doBlocking(IP ip$) {
                return bizImpl$.apply(ip$);
            }

            @Override
            public String name() {
                return name$;
            }
        };
    }

    protected abstract String name();
    protected abstract Single<Either<Violation, OP>> doBiz(IP input$);
    protected final BizException $bizException(Throwable excp)
    {
        return BizException.onException(this.name(), excp);
    }

    public final Single<OP> executeOn(IP input$) {
        Single<Either<Violation, OP>> bizEither = doBiz(input$);
        return bizEither
                .flatMap(either -> {
                    if(either.isLeft())
                        return Single.error(BizException.onViolation(name(), either.left()));
                    else return Single.just(either.right());
                }).onErrorResumeNext( excp -> {
                    if(excp instanceof BizException == false)
                        return Single.error(BizException.onException(name(), excp));
                    else return Single.error(excp);
                });

    }

}
