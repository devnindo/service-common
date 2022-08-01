package io.devnindo.service.deploy.components;

import io.devnindo.service.exec.action.BizAction;
import dagger.MapKey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface ActionKey {
    Class<? extends BizAction> value();
}
