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
package io.devnindo.service.testmains;


import io.reactivex.rxjava3.core.Single;

public class SingleFlowTest {
    public static void main(String[] args)
    {

        Single.just("Okay Just start")
                .doOnSuccess(System.out::println)
                //.flatMap(str -> Single.error(new RuntimeException("Just an error")))
                .flatMap(str -> {
                    System.out.println("Should print: "+str);
                    return Single.just(420);
                })
                .doOnSuccess(System.out::println)
                .doOnError(error -> System.out.println(error.getMessage()))
                .onErrorReturnItem(780)
                .subscribe(System.out::println);
    }
}
