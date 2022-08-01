package io.devnindo.service.deploy.base;

import dagger.Component;

import javax.inject.Singleton;


// compose dependencies that are
// required by both Deploy and Action components
//
@Singleton
@Component(modules = BaseModule.class)
public interface BaseComponent extends BaseDependency {
}
