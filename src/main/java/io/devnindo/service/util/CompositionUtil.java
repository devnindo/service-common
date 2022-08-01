package io.devnindo.service.util;

import io.reactivex.Single;
import io.vertx.reactivex.core.Context;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.Vertx;

import java.util.Objects;

public class CompositionUtil
{
    public static final <T> Single<T> composeVertexScheduler(Single<T> blockingSingle$)
    {
        Vertx vertx = null;
        Context ctx = Vertx.currentContext();
        if(Objects.nonNull(ctx))
            vertx = ctx.owner();

        if(vertx == null)
            return Single.error(new RuntimeException("Blocking step must be run in Vertx context"));

        return blockingSingle$.subscribeOn(RxHelper.blockingScheduler(vertx, false))
                .observeOn(RxHelper.scheduler(vertx));
    }
}
