package io.devnindo.service.testunit.module_example;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.deploy.BizComponentProvider;
import io.devnindo.service.deploy.base.BaseDependency;
import io.devnindo.service.deploy.components.ActionComponent;
import io.devnindo.service.deploy.components.DeployComponent;

public class ComponentProvider implements BizComponentProvider
{
    @Override
    public ActionComponent actionComponent(JsonObject runtimeConfig$, BaseDependency baseDependency)
    {
        return DaggerDummyActionComponent
                .builder()
                .baseDependency(baseDependency)
                .build();
    }
}
