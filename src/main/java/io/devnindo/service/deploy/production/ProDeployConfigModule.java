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
import io.devnindo.service.exec.auth.JWTConfig;
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
 * @author prevy-sage
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
    public JWTConfig sessionHandlerConfig(){ return config.getJwtSessionConfig(); }
    
    @Provides @Singleton
    public BizSessionHandler bizSessionHandler(JWTSessionHandler provider$)
    {
        return provider$;
    }
    
    @Provides @Singleton
    public BizManagerApi managerApi(DummyManagerApi managerApi$)
    {
        return managerApi$;
    }


}
