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
package io.devnindo.service.deploy.dev;

import io.devnindo.service.exec.BizErrorLog;
import io.devnindo.service.exec.BizManagerApi;
import io.devnindo.datatype.json.JsonObject;
import io.reactivex.rxjava3.core.Single;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DummyManagerApi implements BizManagerApi
{

    @Inject
    DummyManagerApi(){

    }
    @Override
    public Single<JsonObject> registerService(JsonObject jo) {
        System.out.println("# PENDING IMPLEMENTATION: A central registration service");
        return Single.just(new JsonObject());
    }


    @Override
    public Single<JsonObject> logError(BizErrorLog errorLog) {
        errorLog.printOnConsole();
        return Single.just(errorLog.toJson());
    }
    
}
