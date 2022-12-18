package io.devnindo.service.deploy.base;

import io.devnindo.service.configmodels.ParamScheduler;
import io.reactivex.rxjava3.core.Scheduler;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;

import javax.inject.Named;

public interface BaseDependency {
    public Vertx vertx();
    public WebClient webClient();

    @Named(ParamScheduler.BLOCKING_SCHEDULER)
    public Scheduler blockingScheduler();
    @Named(ParamScheduler.ASYNC_SCHEDULER)
    public Scheduler asyncScheduler();
}
