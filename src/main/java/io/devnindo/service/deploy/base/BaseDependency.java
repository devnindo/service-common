package io.devnindo.service.deploy.base;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

public interface BaseDependency {
    public Vertx vertx();
    public WebClient webClient();
}
