/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.production;

import io.devnindo.service.deploy.dev.DummyManagerApi;
import io.devnindo.service.exec.auth.BizSessionHandler;
import io.devnindo.service.deploy.components.BeanConfigModule;
import io.devnindo.service.configmodels.ConfigApiClient;
import io.devnindo.service.configmodels.ConfigDeploy;
import io.devnindo.service.configmodels.ProductionDeployConfig;
import io.devnindo.service.exec.auth.JwtConfig;
import io.devnindo.service.exec.auth.JWTSessionHandler;
import io.devnindo.service.exec.BizManagerApi;
import io.devnindo.service.util.DeployParams;
import dagger.Module;
import dagger.Provides;
import io.devnindo.datatype.json.JsonObject;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
@Module
public  class ProDeployConfigModule
        extends BeanConfigModule<ProductionDeployConfig> {

    public ProDeployConfigModule(JsonObject config) {
        super(config, ProductionDeployConfig.class);
    }



    @Provides
    public ConfigDeploy deployConfig()
    {
        return config;
    }


    @Provides
    @Singleton
    @Named(DeployParams.MANAGER_API)
    public ConfigApiClient managerApiConfig()
    {
        return config.getManagerApi();
    }


    
    @Provides @Singleton
    public BizSessionHandler bizSessionHandler()
    {
        return new JWTSessionHandler(config.getJwtSessionConfig());
    }
    
    @Provides @Singleton
    public BizManagerApi managerApi(DummyManagerApi managerApi$)
    {
        return managerApi$;
    }


}
