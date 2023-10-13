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

import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.reactivex.rxjava3.core.Single;

import java.util.function.Function;

public abstract class BizStep<IP, OP> {


    public static  <IP, OP> BizStep<IP, OP> rx(String name$, Function<IP, Single<Either<Violation, OP>>> bizImpl$)
    {
        return new BizStep<IP, OP>() {
            @Override
            public String name() {
                return name$;
            }

            @Override
            public Single<Either<Violation, OP>> doBiz(IP input$) {
                return bizImpl$.apply(input$);
            }
        };
    }

    public static  <IP, OP> BizStep<IP, OP> blocking(String name$, Function<IP,  Either<Violation, OP>> bizImpl$)
    {
        return new BlockingStep<IP, OP>() {
            @Override
            public Either<Violation, OP> doBlocking(IP ip$) {
                return bizImpl$.apply(ip$);
            }

            @Override
            public String name() {
                return name$;
            }
        };
    }

    protected abstract String name();
    protected abstract Single<Either<Violation, OP>> doBiz(IP input$);
    protected final BizException $bizException(Throwable excp)
    {
        return BizException.onException(this.name(), excp);
    }

    public final Single<OP> executeOn(IP input$) {
        Single<Either<Violation, OP>> bizEither = doBiz(input$);
        return bizEither
                .flatMap(either -> {
                    if(either.isLeft())
                        return Single.error(BizException.onViolation(name(), either.left()));
                    else return Single.just(either.right());
                }).onErrorResumeNext( excp -> {
                    if(excp instanceof BizException == false)
                        return Single.error(BizException.onException(name(), excp));
                    else return Single.error(excp);
                });

    }

}
