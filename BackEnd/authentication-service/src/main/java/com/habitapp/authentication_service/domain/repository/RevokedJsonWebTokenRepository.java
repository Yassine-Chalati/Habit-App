package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.RevokedJsonWebToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RevokedJsonWebTokenRepository extends JpaRepository<RevokedJsonWebToken, String> {
    public RevokedJsonWebToken findByJti(String jti);
    /**
     * delete all expired JWT where
     * @param now this parameter as reference time that design the current instant to know the expired JWT
     */
    public void deleteAllByExpirationDateIsBefore(Instant now);
}
