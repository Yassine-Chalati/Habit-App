package com.habitapp.progress_service.domain.service;

import com.habitapp.progress_service.domain.entity.Reward;

public interface RewardService {
    public Reward updateStreak(Long idUser, boolean increment);
    public Reward readStreak(Long idUser);
}
