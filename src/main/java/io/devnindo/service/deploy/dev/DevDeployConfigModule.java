/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.dev;

import io.devnindo.service.exec.auth.BizSessionHandler;
import io.devnindo.service.deploy.components.BeanConfigModule;
import io.devnindo.service.configmodels.ConfigDeploy;
import io.devnindo.service.exec.auth.JwtConfig;
import io.devnindo.service.exec.auth.JWTSessionHandler;
import io.devnindo.service.exec.BizManagerApi;
import dagger.Module;
import dagger.Provides;
import io.devnindo.datatype.json.JsonObject;

import javax.inject.Singleton;

/**
 *
 * @author prevy-sage
 */
//@Module(includes = CommonConfigModule.class)
@Module
public  class DevDeployConfigModule extends BeanConfigModule<ConfigDeploy> {
    
    public DevDeployConfigModule(JsonObject config$) {
        super(config$, ConfigDeploy.class);
    }


    @Provides @Singleton
    public ConfigDeploy deployConfig()
    {
        return config;
    }


    @Provides @Singleton
    public BizSessionHandler bizSessionHandler()
    {
        return new JWTSessionHandler(config.getJwtSessionConfig());
    }

    @Provides @Singleton
    public BizManagerApi managerClient()
    {
        return new DummyManagerApi();
    }


}
