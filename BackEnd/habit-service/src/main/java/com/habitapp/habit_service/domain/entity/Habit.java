package com.habitapp.habit_service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String name;
    private LocalDate toDoDate;
    private boolean checked;

    @OneToMany(mappedBy = "habit", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Goal> goals;

    @ManyToOne
    private Frequency frequency;

    @ManyToOne
    private Remainder remainder;
}
