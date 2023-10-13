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
package io.devnindo.service.exec;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;

import java.util.concurrent.atomic.AtomicBoolean;

public final class RxScheduler
{
    private static  Scheduler blockingScheduler;
    private static  Scheduler asyncScheduler;

    private static AtomicBoolean atomicInit = new AtomicBoolean();

    public static final void init(Scheduler blockingScheduler$, Scheduler asyncScheduler$)
    {
        if (atomicInit.compareAndSet(false, true)) {
            blockingScheduler = blockingScheduler$;
            asyncScheduler = asyncScheduler$;
        }
    }

    public static final <T> Single<T> compose(Single<T> blockingSingle$)
    {


        return blockingSingle$
                .subscribeOn(RxScheduler.blockingScheduler)
                .observeOn(RxScheduler.asyncScheduler);
    }
}
