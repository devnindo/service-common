package action_module.dummy.action;

import action_module.dummy.DummyBlockingAction;
import action_module.dummy.DummyVldModule;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.devnindo.service.deploy.components.ActionKey;
import io.devnindo.service.exec.action.BlockingBizAction;

@Module (includes = DummyVldModule.class)
public interface DummyActionModule {
    @Binds @IntoMap
    @ActionKey(DummyBlockingAction.class)
    BlockingBizAction bind_0(DummyBlockingAction action$);
}
