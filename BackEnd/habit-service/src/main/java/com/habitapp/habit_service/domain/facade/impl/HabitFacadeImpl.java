package com.habitapp.habit_service.domain.facade.impl;

import com.habitapp.habit_service.annotation.Facade;
import com.habitapp.habit_service.domain.entity.Goal;
import com.habitapp.habit_service.domain.entity.Habit;
import com.habitapp.habit_service.domain.facade.HabitFacade;
import com.habitapp.habit_service.domain.repository.GoalRepository;
import com.habitapp.habit_service.domain.service.HabitService;
import com.habitapp.habit_service.proxy.client.progress.ProgressProxy;
import com.habitapp.habit_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.habit_service.proxy.exception.common.UnexpectedException;
import lombok.AllArgsConstructor;

import java.util.List;

@Facade
@AllArgsConstructor
public class HabitFacadeImpl implements HabitFacade {
    private final HabitService habitService;
    private final ProgressProxy progressProxy;
    private final GoalRepository goalRepository;

    @Override
    public List<Habit> readAllHabitsByUserAndCurrentDay(Long idUser) {
        return habitService.readAllHabitsByUserAndCurrentDay(idUser);
    }

    @Override
    public Habit createNewHabit(Habit habit) throws Exception {
        Habit newHabit;
        try {
            newHabit = habitService.createNewHabit(habit);
        } catch (IllegalArgumentException e) {
            throw new Exception(e);
        }
        try {
            progressProxy.saveOrUpdateCharts(habit.getIdUser(), false, false, true, false);
        } catch (UnauthorizedException | UnexpectedException e) {
            habitService.deleteHabit(newHabit.getId());
            throw new Exception(e);
        }
        return newHabit;
    }

    @Override
    public Habit updateHabit(Habit habit) {
        return habitService.updateHabit(habit);
    }

    @Override
    public void deleteHabit(Long idHabit) {
        habitService.deleteHabit(idHabit);
    }

    @Override
    public Habit checkHabit(Long idHabit, boolean checked) throws Exception {
        Habit checkedHabit = habitService.checkHabit(idHabit, checked);
        try {
            progressProxy.saveOrUpdateCharts(checkedHabit.getIdUser(), checked, !checked, false, false);
            progressProxy.updateStreak(checkedHabit.getIdUser(), checked);
        } catch (UnauthorizedException | UnexpectedException e) {
            habitService.checkHabit(idHabit, !checked);
            throw new Exception(e);
        }

        return checkedHabit;
    }

    @Override
    public Habit checkGoal(Long idGoal, boolean checked) throws Exception {
        Goal g = goalRepository.findById(idGoal)
                .orElseThrow( () -> new Exception("Goal not found with id: " + idGoal));
        boolean habitChecked = g.getHabit().isChecked();

        Habit checkedHabit = habitService.checkGoal(idGoal, checked);
        if (checkedHabit.isChecked())
            this.checkHabit(checkedHabit.getId(), true);
        else if (habitChecked)
            this.checkHabit(checkedHabit.getId(), false);

        return checkedHabit;
    }
}
