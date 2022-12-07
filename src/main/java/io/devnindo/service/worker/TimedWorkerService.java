package io.devnindo.service.worker;

import java.util.List;

public class TimedWorkerService
{
    private final List<TimedTask> taskList;

    public TimedWorkerService(List<TimedTask> taskList$)
    {
        taskList = taskList$;
    }

    public void start(){

    }

}
