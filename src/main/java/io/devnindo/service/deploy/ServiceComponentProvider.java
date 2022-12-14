package io.devnindo.service.deploy;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.deploy.base.BaseComponent;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.deploy.components.DeployComponent;
import io.devnindo.service.deploy.dev.DevDeployConfigModule;
import io.devnindo.service.deploy.production.ProDeployConfigModule;

public interface ServiceComponentProvider
{
    BaseComponent baseComponent();
    ActionComponent actionComponent();
    default DeployComponent deployComponent(RuntimeMode runtimeMode$, JsonObject deployConfig){
        if(RuntimeMode.dev.equals(runtimeMode$))
            return DaggerDevDeployComponent
                    .builder()
                    .devDeployConfigModule(new DevDeployConfigModule(deployConfig))
                    .baseDependency(baseComponent)
                    .build();
        else
            return DaggerProDeployComponent
                    .builder()
                    .proDeployConfigModule(new ProDeployConfigModule(deployConfig))
                    .baseDependency(baseComponent)
                    .build();
    }

}
