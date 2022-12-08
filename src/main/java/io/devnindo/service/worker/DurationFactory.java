package io.devnindo.service.worker;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationFactory
{
    public static final Duration atEveryScnd(Long seconds){
        return Duration.of(seconds, ChronoUnit.SECONDS);
    }

    public static final Duration atEveryMin(Long mins){
        return Duration.of(mins, ChronoUnit.MINUTES);
    }

    public static final Duration atEveryHour(Long hrs){
        return Duration.of(hrs, ChronoUnit.HOURS);
    }

    public static final Duration everyDayAt(String clockTime){
        return null; //Duration
    }


}
