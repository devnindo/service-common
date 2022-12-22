package io.devnindo.service.testunit.module_example.dummy;

import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.testunit.module_example.dummy.DummyVldModule;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.devnindo.service.deploy.components.ActionKey;
import io.devnindo.service.exec.action.BlockingBizAction;
import io.devnindo.service.testunit.module_example.dummy.action.DummyBlockingAction;

@Module (includes = {DummyVldModule.class})
public interface DummyActionModule {
    @Binds @IntoMap
    @ActionKey(DummyBlockingAction.class)
    BizAction bind_0(DummyBlockingAction action$);
}
