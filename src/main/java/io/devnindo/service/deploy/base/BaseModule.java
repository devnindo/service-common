package io.devnindo.service.deploy.base;

import io.devnindo.service.BizMain;
import io.devnindo.service.configmodels.ParamScheduler;
import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.components.BeanConfigModule;
import io.devnindo.service.configmodels.ConfigDeploy;
import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vertx.core.VertxOptions;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Module
public class BaseModule extends BeanConfigModule<ConfigDeploy> {

    public BaseModule(JsonObject jsonObj$) {
        super(jsonObj$, ConfigDeploy.class);
    }


    @Provides  @Singleton
    public Vertx vertx()
    {
        return Vertx.vertx();
        //new VertxOptions().setWorkerPoolSize(config.getExecWorkerPoolSize())
    }
    @Provides @Singleton @Named(ParamScheduler.EXEC_BLOCKING_SCHEDULER)
    public Scheduler blockingExecScheduler(){
       ExecutorService execPoolService =  Executors.newFixedThreadPool(config.getExecWorkerPoolSize());
       return Schedulers.from(execPoolService);
    }

    @Provides @Singleton @Named(ParamScheduler.VERTX_CTX_SCHEDULER)
    public Scheduler vertxAsyncScheduler(Vertx vertx){
        return RxHelper.scheduler(vertx);
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
