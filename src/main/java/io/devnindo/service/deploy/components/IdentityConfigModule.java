package io.devnindo.service.deploy.components;

import io.devnindo.service.configmodels.ServiceIdentity;
import io.devnindo.service.util.DeployParams;
import dagger.Module;
import dagger.Provides;
import io.devnindo.core.json.JsonObject;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class IdentityConfigModule extends BeanConfigModule<ServiceIdentity>{

    public IdentityConfigModule(JsonObject config$)
    {
        super(config$, ServiceIdentity.class);
    }

    @Provides @Singleton
    public ServiceIdentity serviceIdentity()
    {
        return config;
    }

    @Provides @Singleton
    @Named(DeployParams.SERVICE_ID)
    public String serviceID(){return config.getId();}

}
