package com.habitapp.habit_service.controller;

import com.habitapp.common.http.request.habit.CheckHabitRequestHttp;
import com.habitapp.habit_service.domain.entity.Habit;
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
    public ResponseEntity<List<Habit>> readAllHabitsByUserAndCurrentDay(@RequestBody Long idUser) {
        List<Habit> habits = habitFacade.readAllHabitsByUserAndCurrentDay(idUser);
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Habit> createHabit(@RequestBody Habit habit) {
        try {
            Habit newHabit = habitFacade.createNewHabit(habit);
            return new ResponseEntity<>(newHabit, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Habit> updateHabit(@RequestBody Habit habit) {
        Habit updatedHabit = null;
        try {
            updatedHabit = habitFacade.updateHabit(habit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(updatedHabit, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteHabit(@RequestBody Long idHabit) {
        habitFacade.deleteHabit(idHabit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/check")
    public ResponseEntity<Habit> checkHabit(@RequestBody CheckHabitRequestHttp checkHabitRequestHttp) {
        try {
            Habit checkedHabit = habitFacade.checkHabit(checkHabitRequestHttp.getIdHabitOrGoal(), checkHabitRequestHttp.isChecked());
            return new ResponseEntity<>(checkedHabit, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/goal/check")
    public ResponseEntity<Habit> checkGoal(@RequestBody CheckHabitRequestHttp checkHabitRequestHttp) {
        try {
            Habit checkedHabit = habitFacade.checkGoal(checkHabitRequestHttp.getIdHabitOrGoal(), checkHabitRequestHttp.isChecked());
            return new ResponseEntity<>(checkedHabit, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
