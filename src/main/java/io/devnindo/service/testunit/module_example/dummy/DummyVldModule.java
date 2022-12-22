package io.devnindo.service.testunit.module_example.dummy;

import dagger.Module;
import dagger.Provides;
import io.devnindo.datatype.schema.BeanValidator;
import io.devnindo.datatype.validation.Validator;
import io.devnindo.service.exec.auth.BizAuth;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class DummyVldModule
{
    interface Role {
        String ADMIN = "admin";
        String SENIOR_EDITOR = "senior_editor";
        String EDITOR = "editor";
    }

    @Provides
    @Singleton
    @Named("DUMMY_SENIOR")
    public static BizAuth seniorManagingAuth() {
        return BizAuth.forRole(Role.ADMIN, Role.SENIOR_EDITOR);
    }

    @Provides
    @Singleton
    @Named("DUMMY_VLD")
    public static BeanValidator<DummyData> dummyVld(){
        return BeanValidator.create("DUMMY_VLD", DummyData.class, c -> {
            c.required($DummyData.NAME);
            c.required($DummyData.SLEEPING_TIME);
        });
    }



}
