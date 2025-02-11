package com.habitapp.progress_service.domain.entity;

import com.habitapp.progress_service.domain.enumiration.WeekDay;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;

}
