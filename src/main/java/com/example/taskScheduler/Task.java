package com.example.taskScheduler;

import lombok.Getter;

public class Task {
    private final Runnable runnable;
    @Getter
    private final long time;

    public Task(Runnable runnable, long time){
        this.runnable = runnable;
        this.time = time;
    }

    public void run(){
        runnable.run();
    }

}
