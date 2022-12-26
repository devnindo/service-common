package io.devnindo.service.util;

public class ThreadUtil {

    public static final void sleepingBlock(int scnds){

        try {
            Thread.sleep(scnds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static final void logCurrent(String blockName$){
        Thread crt = Thread.currentThread();
        String msg = """
                       # %s runs on [%s::%s]
                     """.formatted(blockName$, crt.getName(), crt.hashCode());
        System.out.print(msg);
    }
}
