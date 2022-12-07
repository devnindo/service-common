package io.devnindo.service.worker;

import java.time.Duration;
import java.time.Period;

public interface TimedTask extends Runnable
{
    public Duration intervalDuration();
}
