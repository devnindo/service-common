package io.devnindo.service.deploy;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.BizMain;
import io.devnindo.service.deploy.base.BaseComponent;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.deploy.components.DeployComponent;
import io.devnindo.service.deploy.dev.DaggerDevDeployComponent;
import io.devnindo.service.deploy.dev.DevDeployConfigModule;
import io.devnindo.service.deploy.production.DaggerProDeployComponent;
import io.devnindo.service.deploy.production.ProDeployConfigModule;

public interface ServiceComponentProvider
{
    ActionComponent actionComponent(JsonObject runtimeConfig$);
    default DeployComponent deployComponent(JsonObject deployConfig$){
        if(RuntimeMode.dev.equals(BizMain.runtimeMode()))
            return DaggerDevDeployComponent
                    .builder()
                    .devDeployConfigModule(new DevDeployConfigModule(deployConfig$))
                    .baseDependency(BizMain.baseComponent())
                    .build();
        else
            return DaggerProDeployComponent
                    .builder()
                    .proDeployConfigModule(new ProDeployConfigModule(deployConfig$))
                    .baseDependency(BizMain.baseComponent())
                    .build();
    }

}
