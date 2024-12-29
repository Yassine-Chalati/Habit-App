package com.habitapp.habit_service.client.progress;

import com.habitapp.common.http.request.reward.RewardRequestHttp;
import com.habitapp.common.http.request_response.chart.ChartInforamtionRequestResponseHttp;
import com.habitapp.common.http.request_response.reward.RewardRequestResponseHttp;
import com.habitapp.habit_service.configuration.client.ProgressConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", configuration = ProgressConfiguration.class)
public interface ProgressClient {
    @PostMapping("/reward/update")
    public ResponseEntity<RewardRequestResponseHttp> updateStreak(@RequestBody RewardRequestHttp request);

    @PostMapping("/chart/update")
    public ResponseEntity<?> saveOrUpdateCharts(@RequestBody ChartInforamtionRequestResponseHttp chartInforamtionRequestResponseHttp);
}