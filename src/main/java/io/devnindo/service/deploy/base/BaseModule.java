package io.devnindo.service.deploy.base;

import io.devnindo.service.BizMain;
import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.components.BeanConfigModule;
import io.devnindo.service.configmodels.ConfigDeploy;
import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vertx.core.VertxOptions;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.inject.Singleton;

@Module
public class BaseModule extends BeanConfigModule<ConfigDeploy> {

    public BaseModule(JsonObject jsonObj$) {
        super(jsonObj$, ConfigDeploy.class);
    }


    @Provides  @Singleton
    public Vertx vertx()
    {
        Vertx vertx;
        vertx.executeBlocking()

        return Vertx.vertx();
        //new VertxOptions().setWorkerPoolSize(config.getExecWorkerPoolSize())
    }
    @Provides @Singleton
    public Scheduler execScheduler(){
        if(RuntimeMode.test.equals(BizMain.runtimeMode()))
            return Schedulers.trampoline();
        else // executor pool service might be better ?
            return Schedulers.io();
    }

    @Provides @Singleton
    public WebClient webClient(Vertx vertx)
    {
        return WebClient.create(vertx);
    }

    @Provides @Singleton
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

}
