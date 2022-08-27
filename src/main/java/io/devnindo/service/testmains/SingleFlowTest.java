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
