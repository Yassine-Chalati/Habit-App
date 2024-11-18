package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.ResetPasswordVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordVerificationTokenRepository extends JpaRepository<ResetPasswordVerificationToken, Long> {
}
