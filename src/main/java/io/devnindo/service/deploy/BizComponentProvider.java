package io.devnindo.service.deploy;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.BizMain;
import io.devnindo.service.deploy.base.BaseComponent;
import io.devnindo.service.deploy.base.BaseDependency;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.deploy.components.DeployComponent;
import io.devnindo.service.deploy.dev.DaggerDevDeployComponent;
import io.devnindo.service.deploy.dev.DevDeployConfigModule;
import io.devnindo.service.deploy.production.DaggerProDeployComponent;
import io.devnindo.service.deploy.production.ProDeployConfigModule;

public interface BizComponentProvider
{
    ActionComponent actionComponent(JsonObject runtimeConfig$, BaseDependency baseDependency);


    default DeployComponent devDeployComponent(JsonObject deployConfig$, BaseDependency baseDependency$){
           return DaggerDevDeployComponent
                    .builder()
                    .devDeployConfigModule(new DevDeployConfigModule(deployConfig$))
                    .baseDependency(baseDependency$)
                    .build();

    }

    default DeployComponent proDeployComponent(JsonObject deployConfig$, BaseDependency baseDependency$){
        return DaggerProDeployComponent
                .builder()
                .proDeployConfigModule(new ProDeployConfigModule(deployConfig$))
                .baseDependency(baseDependency$)
                .build();
    }

}
