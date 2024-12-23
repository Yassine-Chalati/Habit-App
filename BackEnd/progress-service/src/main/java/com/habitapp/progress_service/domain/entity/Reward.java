package com.habitapp.progress_service.domain.entity;

import com.habitapp.progress_service.domain.enumiration.Trophy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private int streak;

    @Enumerated(EnumType.STRING)
    private Trophy trophy;
}
