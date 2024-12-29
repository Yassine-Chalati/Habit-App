package com.habitapp.habit_service.domain.facade;

import com.habitapp.habit_service.domain.entity.Habit;
import com.habitapp.habit_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.habit_service.proxy.exception.common.UnexpectedException;

import java.util.List;

public interface HabitFacade {
    public List<Habit> readAllHabitsByUserAndCurrentDay(Long idUser);
    public Habit createNewHabit(Habit habit) throws Exception;
    public Habit updateHabit(Habit habit) throws Exception;
    public void deleteHabit(Long idHabit);

    public Habit checkHabit(Long idHabit, boolean checked) throws Exception;
    public Habit checkGoal(Long idGoal, boolean checked) throws Exception;
}
