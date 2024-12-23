package com.habitapp.authentication_service.common.handler.time;

import java.util.concurrent.TimeUnit;

public class TimeHandler {
    private long startTime;
    private final long fixedMinimumTime;

    public TimeHandler(long fixedMinimumTime) {
        this.fixedMinimumTime = fixedMinimumTime;
    }

    public void start(){
        this.startTime = System.nanoTime();
    }

    public void timingEqualization(){
        long elapsedTime = System.nanoTime() - this.startTime;
        long remainingTime = this.fixedMinimumTime - elapsedTime;

        if (remainingTime > 0){
            try {
                TimeUnit.NANOSECONDS.sleep(remainingTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
