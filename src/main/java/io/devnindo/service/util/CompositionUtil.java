package io.devnindo.service.util;


import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.ContextScheduler;
import io.vertx.rxjava3.core.Context;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;

import java.util.Objects;

public class CompositionUtil
{
    public static final <T> Single<T> composeVertexScheduler(Single<T> blockingSingle$)
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
