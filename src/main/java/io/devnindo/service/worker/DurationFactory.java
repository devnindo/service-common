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
