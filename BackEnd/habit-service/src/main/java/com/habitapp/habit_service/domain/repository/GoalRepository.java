package com.habitapp.habit_service.domain.repository;

import com.habitapp.habit_service.domain.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
}
