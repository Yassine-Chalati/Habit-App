package com.habitapp.habit_service.domain.repository;

import com.habitapp.habit_service.domain.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByIdUserAndToDoDate(Long idUser, LocalDate toDoDate);
}
