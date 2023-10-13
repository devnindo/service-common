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
import io.vertx.rxjava3.ContextScheduler;
import io.vertx.rxjava3.core.Context;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.eventbus.Message;

import java.util.Arrays;

class BlockingTask
{
    public static Single<String> process(Object data$)
    {
        return Single.create( src -> {
            try {
                Thread.sleep(1000 * (Integer) data$);
                System.out.println(Thread.currentThread().getName()+" blocking returning");
                src.onSuccess("BLOCKING RESULT - "+data$);
            } catch (InterruptedException e) {
                src.onError(e);
            }
        });

    }
}
public class RxJavaTest {

    public static final Vertx vertx = Vertx.vertx();

    public static Single<String> transitBlocking(Message<Object> msg){
        return BlockingTask.process(msg.body())
                .subscribeOn(RxHelper.blockingScheduler(vertx, false))
                .observeOn(RxHelper.scheduler(vertx));

    }

    public static Single<String> processMsg(Message<Object> msg)
    {
        return  Single.just((Integer)msg.body())
                .flatMap( data -> {
                    if(data==1)
                        return Single.just("JUST RESULT - 1");
                    else return transitBlocking(msg);
                })
                //.observeOn(RxHelper.scheduler(vertx))
                .doOnSuccess(data -> System.out.println(Thread.currentThread().getName()+" "+data))
                .doOnSuccess(data -> msg.reply(data));
    }

    public static void main(String... args) throws InterruptedException
    {

        /*String queue = "blocking";
        vertx.eventBus().consumer(queue)
                .toObservable()
                .flatMapSingle(RxJavaTest::processMsg)
                .subscribe(System.out::println);

        vertx.eventBus().send(queue, 3 );
        Thread.sleep(700);
        vertx.eventBus().send(queue, 2 );
        vertx.eventBus().send(queue, 1 );*/

        Single<Integer> intSingle = Single.just(1).doOnSuccess(int$ -> System.out.println("ONE : "+int$));
        Single<String> strSingle = Single.just("TWO").doOnSuccess(int$ -> System.out.println("2 : "+int$));
        Single.zip(Arrays.asList(intSingle, strSingle), zippedArr -> {
            return "Completed";
        }).subscribe(System.out::println);

    }
}
