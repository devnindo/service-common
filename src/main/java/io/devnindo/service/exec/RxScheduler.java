package io.devnindo.service.exec;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.ContextScheduler;
import io.vertx.rxjava3.core.Context;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RxScheduler
{
    private static  Scheduler blockingScheduler;
    private static  Scheduler asyncScheduler;

    private static AtomicBoolean atomicInit = new AtomicBoolean();

    public static final void init(Scheduler blockingScheduler$, Scheduler asyncScheduler$)
    {
        if (atomicInit.compareAndSet(false, true)) {
            blockingScheduler = blockingScheduler$;
            asyncScheduler = asyncScheduler$;
        }
    }

    public static final <T> Single<T> composeScheduler(Single<T> blockingSingle$)
    {
        Vertx vertx = null;
        final Context ctx = Vertx.currentContext();
        if(Objects.nonNull(ctx))
            vertx = ctx.owner();

        if(vertx == null)
            return Single.error(new RuntimeException("Blocking step must be run in Vertx context"));

        return blockingSingle$
                .subscribeOn(RxHelper.blockingScheduler(vertx, false))
                .observeOn(new ContextScheduler(ctx.getDelegate(), false));
    }
}
