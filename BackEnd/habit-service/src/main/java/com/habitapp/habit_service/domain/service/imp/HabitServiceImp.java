package com.habitapp.habit_service.domain.service.imp;

import com.habitapp.habit_service.domain.entity.Goal;
import com.habitapp.habit_service.domain.entity.Habit;
import com.habitapp.habit_service.domain.repository.GoalRepository;
import com.habitapp.habit_service.domain.repository.HabitRepository;
import com.habitapp.habit_service.domain.service.HabitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class HabitServiceImp implements HabitService {

    private final HabitRepository habitRepository;
    private final GoalRepository goalRepository;

    @Override
    public List<Habit> readAllHabitsByUserAndCurrentDay(Long idUser) {
        LocalDate currentDate = LocalDate.now();
        return habitRepository.findAllByIdUserAndToDoDate(idUser, currentDate);
    }

    @Override
    public Habit createNewHabit(Habit habit) {
        habit.setToDoDate(LocalDate.now());
        habit.setChecked(false);
        habit.setId(0L);
        habit.getGoals().forEach(goal -> goal.setChecked(false));

        return habitRepository.save(habit);
    }

    @Override
    public Habit updateHabit(Habit habit) {
        return habitRepository.findById(habit.getId()).map(h -> {
            h.setName(habit.getName());
            h.setFrequency(habit.getFrequency());
            h.setRemainder(habit.getRemainder());
            return habitRepository.save(habit);
        }).orElseThrow(() -> new IllegalArgumentException("Habit not found with id: " + habit.getId()));
    }

    @Override
    public void deleteHabit(Long idHabit) {
        habitRepository.deleteById(idHabit);
    }

    @Override
    public Habit checkHabit(Long idHabit, boolean checked) {
        return habitRepository.findById(idHabit).map(habit -> {
            habit.setChecked(checked);
            return habitRepository.save(habit);
        }).orElseThrow(() -> new IllegalArgumentException("Habit not found with id: " + idHabit));
    }

    @Override
    public Habit checkGoal(Long idGoal, boolean checked) {
        boolean allGoalsChecked = true;
        Habit habit = null;
        Goal go = goalRepository.findById(idGoal).map(goal -> {
            goal.setChecked(checked);
            return goalRepository.save(goal);
        }).orElseThrow(() -> new IllegalArgumentException("Goal not found with id: " + idGoal));

        for (Goal g : go.getHabit().getGoals()){
            if (!g.getChecked()){
                allGoalsChecked = false;
                if (g.getHabit().isChecked())
                    g.getHabit().setChecked(false);
                break;
            }
        }

        if (allGoalsChecked){
            go.getHabit().setChecked(true);
            habit = habitRepository.save(go.getHabit());
        }

        return habit == null ? go.getHabit() : habit;
    }
}
