package com.habitapp.progress_service.controller;

import com.habitapp.common.http.request.reward.RewardRequestHttp;
import com.habitapp.common.http.request_response.reward.RewardRequestResponseHttp;
import com.habitapp.progress_service.domain.entity.Reward;
import com.habitapp.progress_service.domain.enumiration.Trophy;
import com.habitapp.progress_service.domain.service.RewardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
    @RequestMapping("/reward")
public class RewardController {
    private final RewardService rewardService;

    @PostMapping("/read")
    public ResponseEntity<RewardRequestResponseHttp> readStreak(@RequestBody    Long idUser) {
        RewardRequestResponseHttp reward = this.mapToRewardRequestResponse(rewardService.readStreak(idUser));
        return new ResponseEntity<>(reward, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<RewardRequestResponseHttp> updateStreak(@RequestBody RewardRequestHttp request) {
        RewardRequestResponseHttp reward = this.mapToRewardRequestResponse(rewardService.updateStreak(request.getIdUser(), request.isIncrement()));
        return new ResponseEntity<>(reward, new HttpHeaders(), HttpStatus.OK);
    }

    private String trophyToString(Trophy trophy) {
        return trophy != null ? trophy.name() : Trophy.BRONZE.name();
    }

    private Trophy stringToTrophy(String trophy) {
        try {
            return trophy != null ? Trophy.valueOf(trophy.toUpperCase()) : Trophy.BRONZE;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Trophy value: " + trophy);
        }
    }

    private Reward mapToReward(RewardRequestResponseHttp requestResponse) {
        Reward reward = new Reward();
        reward.setIdUser(requestResponse.getIdUser());
        reward.setStreak(requestResponse.getStreak());

        if (requestResponse.getTrophy() != null) {
            reward.setTrophy(Trophy.fromString(requestResponse.getTrophy()));
        }

        return reward;
    }

    private RewardRequestResponseHttp mapToRewardRequestResponse(Reward reward) {
        RewardRequestResponseHttp response = new RewardRequestResponseHttp();
        response.setIdUser(reward.getIdUser());
        response.setStreak(reward.getStreak());

        // Convert the Trophy enum to its String representation
        if (reward.getTrophy() != null) {
            response.setTrophy(reward.getTrophy().name());
        }

        return response;
    }
}
