package action_module;

import action_module.dummy.action.DummyActionModule;
import dagger.Component;
import io.devnindo.service.deploy.base.BaseDependency;
import io.devnindo.service.deploy.components.ActionComponent;

@Component(modules={DummyActionModule.class}, dependencies = BaseDependency.class)
public interface DummyActionComponent extends ActionComponent {
}
