package io.devnindo.service.deploy.base;

import io.reactivex.rxjava3.core.Scheduler;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;

public interface BaseDependency {
    public Vertx vertx();
    public WebClient webClient();

    public Scheduler execScheduler();
}
