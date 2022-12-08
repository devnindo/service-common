package io.devnindo.service.worker;

import java.time.Duration;

public class ABlockingTask implements TimedTask
{
    @Override
    public Duration intervalDuration() {
        return  null;
    }

    @Override
    public void run() {

    }
}
