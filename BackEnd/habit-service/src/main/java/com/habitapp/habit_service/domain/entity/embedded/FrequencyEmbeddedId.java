package com.habitapp.habit_service.domain.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class FrequencyEmbeddedId implements Serializable {
    private boolean daily;
    private boolean weekly;
    private boolean monthly;
}
