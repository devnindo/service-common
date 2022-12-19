package io.devnindo.service.exec;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;

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

    public static final <T> Single<T> compose(Single<T> blockingSingle$)
    {


        return blockingSingle$
                .subscribeOn(RxScheduler.blockingScheduler)
                .observeOn(RxScheduler.asyncScheduler);
    }
}
