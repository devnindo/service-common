/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.components;

import io.devnindo.service.configmodels.ServiceIdentity;
import io.devnindo.service.deploy.ServiceDeployer;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.exec.BizExecutor;
import dagger.Component;
import io.vertx.rxjava3.core.Vertx;

import java.util.Map;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
@Singleton
@Component(modules=IdentityConfigModule.class, dependencies = {DeployComponent.class, ActionComponent.class})
public interface BizComponent {
    Vertx vertx();
    ServiceIdentity identity();
    BizExecutor executor();
    ServiceDeployer serverDeployer();
    Map<Class<?extends BizAction>, Provider<BizAction>> actionSet();

    /*@Component.Builder
    interface Builder {
        Builder identity(IdentityConfigModule module);
        Builder deployComponent(DeployComponent component);
        Builder actionComponent(ActionComponent component);
        BizComponent build();
    }*/
}
