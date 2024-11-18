package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.VerificationToken;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ActivationAccountVerificationToken extends VerificationToken {
    public ActivationAccountVerificationToken(String token, LocalDateTime creationDate, DefaultAccountCandidate candidate){
        super(token, creationDate, candidate);
    }
}
