package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.VerificationToken;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
public class ResetPasswordVerificationToken extends VerificationToken {
}
