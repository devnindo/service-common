package io.devnindo.service.testunit;

import java.util.concurrent.CountDownLatch;

public class SimpleCase
{
    private final CountDownLatch latch;
    SimpleCase(CountDownLatch latch$){
        latch = latch$;
    }
    public void execute(long waitingTime, Runnable assertion)
    {
        AssertionError[] errorArr = {null};
        new Thread(()-> {
            try {
                Thread.sleep(waitingTime);
                System.out.println("# Simple Case executed: "+waitingTime);
                latch.countDown();
                assertion.run();
            } catch (InterruptedException e) {
               e.printStackTrace();
            } catch (AssertionError error){
                 errorArr[0] = error;
            }
        }).run();

        if(errorArr[0] != null)
            errorArr[0].printStackTrace();

    }
}
