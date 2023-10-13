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
