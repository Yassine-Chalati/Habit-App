package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.ActivationAccountVerificationToken;
import com.menara.authentication.domain.entity.DefaultAccountCandidate;
import com.menara.authentication.domain.base.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationAccountVerificationTokenRepository extends JpaRepository<ActivationAccountVerificationToken, Long> {
}
