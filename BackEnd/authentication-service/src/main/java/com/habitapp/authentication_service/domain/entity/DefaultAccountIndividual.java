package com.habitapp.authentication_service.domain.entity;

import com.habitapp.authentication_service.domain.base.DefaultAccount;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DefaultAccountIndividual extends DefaultAccount {
    private boolean activated = false;
    @OneToOne(orphanRemoval = true, mappedBy = "individual", cascade = CascadeType.ALL)
    private ActivationAccountVerificationToken activationAccountVerificationToken;

    public DefaultAccountIndividual(String email, String password, LocalDateTime creationDate, ActivationAccountVerificationToken activationAccountVerificationToken){
        super(email, password, creationDate, null, null);
        this.activationAccountVerificationToken = activationAccountVerificationToken;
    }

    public DefaultAccountIndividual(String email, String password, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions, ActivationAccountVerificationToken activationAccountVerificationToken){
        super(email, password, creationDate, roles, permissions);
        this.activationAccountVerificationToken = activationAccountVerificationToken;
    }

    public DefaultAccountIndividual(long id, String email, String password, LocalDateTime creationDate, ActivationAccountVerificationToken activationAccountVerificationToken){
        super(id, email, password, creationDate);
        this.activationAccountVerificationToken = activationAccountVerificationToken;
    }
}
