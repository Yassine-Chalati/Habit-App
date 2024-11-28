package com.habitapp.habit_service.scheduler;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.AllArgsConstructor;

// @Scheduler
@AllArgsConstructor
public class SchedulerNameScheduler {

    @Scheduled(fixedDelayString = "${configuration.path.in.application.or.bootstrap.properties.or.yaml}", timeUnit = TimeUnit.SECONDS)
    public void actionName(){
        
    }
}
