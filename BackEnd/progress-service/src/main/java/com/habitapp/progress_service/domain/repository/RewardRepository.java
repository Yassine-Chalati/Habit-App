package com.habitapp.progress_service.domain.repository;

import com.habitapp.progress_service.domain.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

}
