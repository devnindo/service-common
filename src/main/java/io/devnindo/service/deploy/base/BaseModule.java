package io.devnindo.service.deploy.base;

import io.devnindo.service.deploy.components.BeanConfigModule;
import io.devnindo.service.configmodels.ConfigDeploy;
import dagger.Module;
import dagger.Provides;
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
        return Vertx.vertx(new VertxOptions().setWorkerPoolSize(config.getExecWorkerPoolSize()));
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
