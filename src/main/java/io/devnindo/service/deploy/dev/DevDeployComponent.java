/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.dev;

import io.devnindo.service.deploy.base.BaseDependency;
import io.devnindo.service.deploy.components.DeployComponent;
import dagger.Component;

import javax.inject.Singleton;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
@Singleton
@Component(modules=DevDeployConfigModule.class, dependencies = BaseDependency.class)
public interface DevDeployComponent extends DeployComponent {



}
