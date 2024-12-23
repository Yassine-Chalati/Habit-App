package com.habitapp.authentication_service.domain.base;

import com.habitapp.authentication_service.domain.entity.DefaultAccountIndividual;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private LocalDateTime creationDate;
    @OneToOne
    private DefaultAccountIndividual individual;

    public VerificationToken(String token, LocalDateTime creationDate, DefaultAccountIndividual individual) {
        this.token = token;
        this.creationDate = creationDate;
        this.individual = individual;
    }
}
