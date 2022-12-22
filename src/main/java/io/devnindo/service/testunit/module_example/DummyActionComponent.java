package io.devnindo.service.testunit.module_example;

import io.devnindo.service.testunit.module_example.dummy.DummyActionModule;
import dagger.Component;
import io.devnindo.service.deploy.base.BaseDependency;
import io.devnindo.service.deploy.components.ActionComponent;

import javax.inject.Singleton;

@Singleton
@Component(modules={DummyActionModule.class}, dependencies = BaseDependency.class)
public interface DummyActionComponent extends ActionComponent {
}
