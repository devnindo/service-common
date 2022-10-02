package io.devnindo.service.worker;

import java.time.Period;

public interface TimedTask
{
    public Period period();
    public void run();
}
