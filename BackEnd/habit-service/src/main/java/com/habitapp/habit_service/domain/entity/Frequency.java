package com.habitapp.habit_service.domain.entity;

import com.habitapp.habit_service.domain.entity.embedded.FrequencyEmbeddedId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Frequency {
    @EmbeddedId
    private FrequencyEmbeddedId id;

    @OneToMany(mappedBy = "frequency")
    private List<Habit> habits;
}
