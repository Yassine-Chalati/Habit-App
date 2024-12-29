package com.habitapp.habit_service.domain.service;

import com.habitapp.habit_service.domain.entity.Habit;

import java.util.List;

public interface HabitService {
    public List<Habit> readAllHabitsByUserAndCurrentDay(Long idUser);
    public Habit createNewHabit(Habit habit);
    public Habit updateHabit(Habit habit);
    public void deleteHabit(Long idHabit);

    public Habit checkHabit(Long idHabit, boolean checked);
    public Habit checkGoal(Long idGoal, boolean checked);
}
