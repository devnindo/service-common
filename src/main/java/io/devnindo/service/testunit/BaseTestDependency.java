/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            /*vertx.close()
                .doOnComplete(()-> System.out.println("# Vertx is closed"))
                .subscribe();*/
        }));
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
