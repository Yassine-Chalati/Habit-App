package com.habitapp.progress_service.domain.service.imp;

import com.habitapp.progress_service.domain.entity.Reward;
import com.habitapp.progress_service.domain.enumiration.Trophy;
import com.habitapp.progress_service.domain.repository.RewardRepository;
import com.habitapp.progress_service.domain.service.RewardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RewardServiceImp implements RewardService {
    private final RewardRepository rewardRepository;

    public Reward updateStreak(Long idUser, boolean increment) {
        Reward reward = rewardRepository.findById(idUser)
                .orElse(new Reward(idUser, 0, Trophy.BRONZE));

        if (increment) {
            reward.setStreak(reward.getStreak() + 1);
        } else {
            reward.setStreak(Math.max(0, reward.getStreak() - 1));
        }

        reward.setTrophy(determineTrophy(reward.getStreak()));
        return rewardRepository.save(reward);
    }

    @Override
    public Reward readStreak(Long idUser) {
        Reward reward = rewardRepository.findById(idUser)
                .orElse(new Reward(idUser, 0, Trophy.BRONZE));


        return reward;
    }

    private Trophy determineTrophy(int streak) {
        if (streak >= 30) {
            return Trophy.DIAMOND;
        } else if (streak >= 20) {
            return Trophy.PLATINUM;
        } else if (streak >= 10) {
            return Trophy.GOLD;
        } else if (streak >= 5) {
            return Trophy.SILVER;
        } else {
            return Trophy.BRONZE;
        }
    }
}
