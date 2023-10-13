/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.service.testunit.module_example.dummy;

import dagger.Module;
import dagger.Provides;
import io.devnindo.datatype.schema.BeanValidator;
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
