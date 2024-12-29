package com.habitapp.habit_service.domain.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequencyEmbeddedId implements Serializable {
    private boolean daily;
    private boolean weekly;
    private boolean monthly;
}
