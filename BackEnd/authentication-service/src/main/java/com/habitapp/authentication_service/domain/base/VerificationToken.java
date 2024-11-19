package com.menara.authentication.domain.base;

import com.menara.authentication.domain.entity.DefaultAccountCandidate;
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
    private DefaultAccountCandidate candidate;

    public VerificationToken(String token, LocalDateTime creationDate, DefaultAccountCandidate candidate) {
        this.token = token;
        this.creationDate = creationDate;
        this.candidate = candidate;
    }
}
