package io.devnindo.service.worker;

import io.vertx.rxjava3.core.Vertx;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;


public class TimerTest {

    static final Vertx vertx = Vertx.vertx();
    static CountDownLatch latch = new CountDownLatch(100);
    static Random random = new Random();
    static int evenCount= 0;
    public static void checkCloseTask(){
        for(int idx=0; idx <= 10000; idx++){
            if(random.nextInt() % 101 == 0) {
                try {
                    Thread.sleep(10);
                    evenCount++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        latch.countDown();

        if(latch.getCount() >0)
            timeTask();
    }

    public static void timeTask(){
        vertx.setTimer(100, h-> checkCloseTask());

    }

    public static ConcurrentMap<String, Integer> test(CountDownLatch latch$){
        ConcurrentMap<String, Integer> evtTaskCount = new ConcurrentHashMap<>();
        long tryCount = latch$.getCount();
        for(int idx = 0; idx < tryCount; idx++)
        {
            vertx.setTimer(1000+idx, h-> {
                try{
                    Thread.sleep(5);
                    String name = Thread.currentThread().getName();
                    Integer count = evtTaskCount.get(name);
                    if(count == null)
                        count = 0;
                    evtTaskCount.put(name, ++count);
                    latch$.countDown();;
                }catch (InterruptedException excp){
                }
            });
        }
        return evtTaskCount;
    }

    public static void main(String... args) throws InterruptedException {

        /*long nanoStart = System.nanoTime();
        timeTask();
        latch.await();
        System.out.println("Total closed: "+evenCount);
        System.out.println("Elapsed time: "+((System.nanoTime() - nanoStart) / 1_000_000));
        vertx.rxClose().subscribe();
        if(true)return;*/
        try {
            CountDownLatch latch = new CountDownLatch(10000);
            long currentNanos = System.nanoTime();
            ConcurrentMap<String, Integer> evtMap = test(latch);
            latch.await();

            System.out.println("Elapsed time: "+((System.nanoTime() - currentNanos) / 1_000_000));
            System.out.println(evtMap.size());
            evtMap.entrySet().forEach(entry ->{
                System.out.println(entry.getKey()+" "+entry.getValue());
            });

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {

            vertx.rxClose().subscribe();
        }
    }
}
