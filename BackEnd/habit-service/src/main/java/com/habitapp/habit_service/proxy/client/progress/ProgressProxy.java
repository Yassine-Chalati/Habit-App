package com.habitapp.habit_service.proxy.client.progress;


import com.habitapp.common.http.request.reward.RewardRequestHttp;
import com.habitapp.common.http.request_response.chart.ChartInforamtionRequestResponseHttp;
import com.habitapp.habit_service.annotation.Proxy;
import com.habitapp.habit_service.client.progress.ProgressClient;
import com.habitapp.habit_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.habit_service.proxy.exception.common.UnexpectedException;
import feign.FeignException;
import lombok.AllArgsConstructor;

@Proxy
@AllArgsConstructor
public class ProgressProxy {
    private ProgressClient progressClient;

    public void updateStreak(long idUser, boolean increment ) throws UnauthorizedException, UnexpectedException {
        try {
            progressClient.updateStreak(new RewardRequestHttp(idUser, increment));
        } catch (FeignException e) {
            handleFeignException(e, "update Streak");
        }
    }


    public void saveOrUpdateCharts(long idUser, boolean checked, boolean unchecked, boolean newHabit, boolean deleteHabit) throws UnauthorizedException, UnexpectedException {
        try {
            progressClient.saveOrUpdateCharts(new ChartInforamtionRequestResponseHttp(idUser, checked, unchecked, newHabit, deleteHabit));
        } catch (FeignException e) {
            handleFeignException(e, "save or update Charts");
        }
    }


    private void handleFeignException(FeignException e, String action) throws UnauthorizedException, UnexpectedException {
        if (e.status() == 401) {
            throw new UnauthorizedException("Unauthorized to " + action);
        }
        throw new UnexpectedException("Unexpected error during " + action + " - HTTP status: " + e.status());
    }
}
