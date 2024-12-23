package com.habitapp.authentication_service.domain.repository;

import com.habitapp.authentication_service.domain.entity.ActivationAccountVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationAccountVerificationTokenRepository extends JpaRepository<ActivationAccountVerificationToken, Long> {
}
