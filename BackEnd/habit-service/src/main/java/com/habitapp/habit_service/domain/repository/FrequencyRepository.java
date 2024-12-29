package com.habitapp.habit_service.domain.repository;

import com.habitapp.habit_service.domain.entity.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequencyRepository extends JpaRepository<Frequency, Long> {
}
