/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.production;

import io.devnindo.service.deploy.base.BaseDependency;
import io.devnindo.service.deploy.components.DeployComponent;
import dagger.Component;

import javax.inject.Singleton;

/**
 *
 * @author prevy-sage
 */
@Singleton
@Component(modules = ProDeployConfigModule.class, dependencies = BaseDependency.class)
public interface ProDeployComponent extends DeployComponent {

}
