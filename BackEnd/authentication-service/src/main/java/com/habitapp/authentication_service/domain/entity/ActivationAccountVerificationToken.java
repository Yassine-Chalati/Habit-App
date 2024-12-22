package com.habitapp.authentication_service.domain.entity;

import com.habitapp.authentication_service.domain.base.VerificationToken;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ActivationAccountVerificationToken extends VerificationToken {
    public ActivationAccountVerificationToken(String token, LocalDateTime creationDate, DefaultAccountIndividual individual){
        super(token, creationDate, individual);
    }
}
