package com.habitapp.habit_service.controller;

import com.habitapp.common.http.request.habit.CheckHabitRequestHttp;
import com.habitapp.common.http.request_response.habit.GoalRequestResponseHttp;
import com.habitapp.common.http.request_response.habit.HabitRequestResponseHttp;
import com.habitapp.habit_service.domain.entity.Frequency;
import com.habitapp.habit_service.domain.entity.Goal;
import com.habitapp.habit_service.domain.entity.Habit;
import com.habitapp.habit_service.domain.entity.Remainder;
import com.habitapp.habit_service.domain.entity.embedded.FrequencyEmbeddedId;
import com.habitapp.habit_service.domain.facade.HabitFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habit")
@AllArgsConstructor
public class HabitController {
    private final HabitFacade habitFacade;

    @GetMapping("/read/today")
    public ResponseEntity<List<HabitRequestResponseHttp>> readAllHabitsByUserAndCurrentDay(@RequestBody Long idUser) {
        List<HabitRequestResponseHttp> habits = habitFacade.readAllHabitsByUserAndCurrentDay(idUser).stream().map(newHabit -> new HabitRequestResponseHttp(newHabit.getId(), newHabit.getIdUser(), newHabit.getName(), newHabit.isChecked(), newHabit.getFrequency().getId().isDaily(), newHabit.getFrequency().getId().isWeekly(), newHabit.getFrequency().getId().isMonthly(), newHabit.getRemainder().getDate(), newHabit.getGoals().stream().map(goal -> new GoalRequestResponseHttp(goal.getId(), goal.getName(), goal.getChecked())).toList())).toList();
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HabitRequestResponseHttp> createHabit(@RequestBody HabitRequestResponseHttp habit) {
        try {
            Habit newHabit = habitFacade.createNewHabit(new Habit(0L, habit.getIdUser(), habit.getName(), null, false, habit.getGoals().stream().map(goal -> new Goal(0L, goal.getName(), false, null)).toList(), new Frequency(new FrequencyEmbeddedId(habit.isDaily(), habit.isWeekly(), habit.isMonthly())), new Remainder(habit.getRemainder())));
            return new ResponseEntity<>(new HabitRequestResponseHttp(newHabit.getId(), newHabit.getIdUser(), newHabit.getName(), newHabit.isChecked(), newHabit.getFrequency().getId().isDaily(), newHabit.getFrequency().getId().isWeekly(), newHabit.getFrequency().getId().isMonthly(), newHabit.getRemainder().getDate(), newHabit.getGoals().stream().map(goal -> new GoalRequestResponseHttp(goal.getId(), goal.getName(), goal.getChecked())).toList()), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<HabitRequestResponseHttp> updateHabit(@RequestBody HabitRequestResponseHttp habit) {
        Habit updatedHabit = null;
        try {
            updatedHabit = habitFacade.updateHabit(new Habit(habit.getId(), habit.getIdUser(), habit.getName(), null, habit.isChecked(), habit.getGoals().stream().map(goal -> new Goal(goal.getId(), goal.getName(), goal.getChecked(), null)).toList(), new Frequency(new FrequencyEmbeddedId(habit.isDaily(), habit.isWeekly(), habit.isMonthly())), new Remainder(habit.getRemainder())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(new HabitRequestResponseHttp(updatedHabit.getId(), updatedHabit.getIdUser(), updatedHabit.getName(), updatedHabit.isChecked(), updatedHabit.getFrequency().getId().isDaily(), updatedHabit.getFrequency().getId().isWeekly(), updatedHabit.getFrequency().getId().isMonthly(), updatedHabit.getRemainder().getDate(), updatedHabit.getGoals().stream().map(goal -> new GoalRequestResponseHttp(goal.getId(), goal.getName(), goal.getChecked())).toList()), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteHabit(@RequestBody Long idHabit) {
        habitFacade.deleteHabit(idHabit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/check")
    public ResponseEntity<HabitRequestResponseHttp> checkHabit(@RequestBody CheckHabitRequestHttp checkHabitRequestHttp) {
        try {
            Habit checkedHabit = habitFacade.checkHabit(checkHabitRequestHttp.getIdHabitOrGoal(), checkHabitRequestHttp.isChecked());
            return new ResponseEntity<>(new HabitRequestResponseHttp(checkedHabit.getId(), checkedHabit.getIdUser(), checkedHabit.getName(), checkedHabit.isChecked(), checkedHabit.getFrequency().getId().isDaily(), checkedHabit.getFrequency().getId().isWeekly(), checkedHabit.getFrequency().getId().isMonthly(), checkedHabit.getRemainder().getDate(), checkedHabit.getGoals().stream().map(goal -> new GoalRequestResponseHttp(goal.getId(), goal.getName(), goal.getChecked())).toList()), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/goal/check")
    public ResponseEntity<HabitRequestResponseHttp> checkGoal(@RequestBody CheckHabitRequestHttp checkHabitRequestHttp) {
        try {
            Habit checkedHabit = habitFacade.checkGoal(checkHabitRequestHttp.getIdHabitOrGoal(), checkHabitRequestHttp.isChecked());
            return new ResponseEntity<>(new HabitRequestResponseHttp(checkedHabit.getId(), checkedHabit.getIdUser(), checkedHabit.getName(), checkedHabit.isChecked(), checkedHabit.getFrequency().getId().isDaily(), checkedHabit.getFrequency().getId().isWeekly(), checkedHabit.getFrequency().getId().isMonthly(), checkedHabit.getRemainder().getDate(), checkedHabit.getGoals().stream().map(goal -> new GoalRequestResponseHttp(goal.getId(), goal.getName(), goal.getChecked())).toList()), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
