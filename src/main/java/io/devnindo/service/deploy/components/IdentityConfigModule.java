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
package io.devnindo.service.deploy.components;

import io.devnindo.service.configmodels.ServiceIdentity;
import io.devnindo.service.util.DeployParams;
import dagger.Module;
import dagger.Provides;
import io.devnindo.datatype.json.JsonObject;

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
