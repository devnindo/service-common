package io.devnindo.service.testunit;

import io.devnindo.service.deploy.base.BaseDependency;
import io.devnindo.service.exec.RxScheduler;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vertx.core.VertxOptions;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;

public class BaseTestDependency implements BaseDependency
{

    private final Vertx vertx;
    BaseTestDependency(){
        VertxOptions vertxOptions = new VertxOptions().setEventLoopPoolSize(1);
        vertx = Vertx.vertx(vertxOptions);
    }
    @Override
    public Vertx vertx() {
        return vertx;
    }

    @Override
    public WebClient webClient() {
        return null;
    }

    @Override
    public Scheduler blockingScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler asyncScheduler() {
        return RxHelper.scheduler(vertx);
    }
}
