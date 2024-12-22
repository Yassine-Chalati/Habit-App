package com.habitapp.authentication_service.scheduler;

import com.habitapp.authentication_service.annotation.Scheduler;
import com.habitapp.authentication_service.domain.facade.AuthenticationFacade;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Scheduler
@AllArgsConstructor
public class DeleteJsonWebTokenDataScheduler {
    private AuthenticationFacade authenticationFacade;

    @Scheduled(fixedDelayString = "${scheduler.jwt.delete.duration}", timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void deleteJsonWebTokenData(){
        System.out.println("scheduler jwt");
        authenticationFacade.removeExpiredJsonWebToken();
    }
}
