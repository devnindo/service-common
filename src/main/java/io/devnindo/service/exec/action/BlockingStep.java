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
package io.devnindo.service.exec.action;

import io.devnindo.service.exec.RxScheduler;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.reactivex.rxjava3.core.Single;

public abstract class  BlockingStep<IP, OP> extends BizStep<IP, OP>
{
    protected abstract   Either<Violation, OP> doBlocking(IP ip$);

    @Override
    protected final Single<Either<Violation, OP>> doBiz(IP input$)
    {

        Single<Either<Violation, OP>> bizSingle =
                Single.create
                (
                        src ->
                        {
                            try {
                                Either<Violation, OP> either = doBlocking(input$);
                                src.onSuccess(either);
                            }catch (Exception excp)
                            {
                                if(excp instanceof BizException)
                                    src.onError(excp);
                                else src.onError(BizException.onException(name(), excp));
                            }
                        }
                );

        return RxScheduler.compose(bizSingle)
                // here only possible exception would be RuntimeException due to null-vertx-context
                .onErrorResumeNext(error -> Single.error(this.$bizException(error)));

    }


}
