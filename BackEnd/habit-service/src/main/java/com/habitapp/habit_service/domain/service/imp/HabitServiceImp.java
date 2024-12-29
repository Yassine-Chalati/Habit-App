package com.habitapp.habit_service.domain.service.imp;

import com.habitapp.habit_service.domain.entity.Frequency;
import com.habitapp.habit_service.domain.entity.Goal;
import com.habitapp.habit_service.domain.entity.Habit;
import com.habitapp.habit_service.domain.entity.Remainder;
import com.habitapp.habit_service.domain.repository.FrequencyRepository;
import com.habitapp.habit_service.domain.repository.GoalRepository;
import com.habitapp.habit_service.domain.repository.HabitRepository;
import com.habitapp.habit_service.domain.repository.RemainderRepository;
import com.habitapp.habit_service.domain.service.HabitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HabitServiceImp implements HabitService {

    private final HabitRepository habitRepository;
    private final GoalRepository goalRepository;
    private final FrequencyRepository frequencyRepository;
    private final RemainderRepository remainderRepository;

    @Override
    public List<Habit> readAllHabitsByUserAndCurrentDay(Long idUser) {
        LocalDate currentDate = LocalDate.now();
        return habitRepository.findAllByIdUserAndToDoDate(idUser, currentDate);
    }

    @Override
    public Habit createNewHabit(Habit habit) {
        habitRepository.findById(habit.getId()).ifPresent(h -> {
            throw new IllegalArgumentException("Habit already exists with id: " + habit.getId());
        });

        habit.setToDoDate(LocalDate.now());
        habit.setChecked(false);
        habit.setId(0L);
        habit.getGoals().forEach(goal -> goal.setChecked(false));
        frequencyRepository.save(habit.getFrequency());
        remainderRepository.save(habit.getRemainder());

        Habit savedHabit = habitRepository.save(habit);

        for (Goal goal : savedHabit.getGoals()){
            goal.setHabit(savedHabit);
            goalRepository.save(goal);
        }

        return savedHabit;
    }

    @Override
    public Habit updateHabit(Habit habit) {
        return habitRepository.findById(habit.getId()).map(h -> {
            h.setName(habit.getName());

            Frequency newFrequency = habit.getFrequency();
            if (newFrequency != null) {
                Frequency persistedFrequency = frequencyRepository.findById(newFrequency.getId())
                        .orElseGet(() -> frequencyRepository.save(newFrequency));
                h.setFrequency(persistedFrequency);
            }

            Remainder newRemainder = habit.getRemainder();
            if (newRemainder != null) {
                Remainder persistedRemainder = remainderRepository.findById(newRemainder.getDate())
                        .orElseGet(() -> remainderRepository.save(newRemainder));
                h.setRemainder(persistedRemainder);
            }

            return habitRepository.save(h);
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
