package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.DefaultAccount;
import com.menara.authentication.domain.base.VerificationToken;
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
public class DefaultAccountCandidate extends DefaultAccount {
    private boolean activated = false;
    @OneToOne(orphanRemoval = true, mappedBy = "candidate", cascade = CascadeType.ALL)
    private ActivationAccountVerificationToken activationAccountVerificationToken;
    @OneToOne(orphanRemoval = true, mappedBy = "candidate", cascade = CascadeType.ALL)
    private ResetPasswordVerificationToken resetPasswordVerificationToken;

    public DefaultAccountCandidate(String email, String password, LocalDateTime creationDate, ActivationAccountVerificationToken activationAccountVerificationToken){
        super(email, password, creationDate, null, null);
        this.activationAccountVerificationToken = activationAccountVerificationToken;
    }

    public DefaultAccountCandidate(String email, String password, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions, ActivationAccountVerificationToken activationAccountVerificationToken){
        super(email, password, creationDate, roles, permissions);
        this.activationAccountVerificationToken = activationAccountVerificationToken;
    }

    public DefaultAccountCandidate(long id, String email, String password, LocalDateTime creationDate, ActivationAccountVerificationToken activationAccountVerificationToken){
        super(id, email, password, creationDate);
        this.activationAccountVerificationToken = activationAccountVerificationToken;
    }
}
