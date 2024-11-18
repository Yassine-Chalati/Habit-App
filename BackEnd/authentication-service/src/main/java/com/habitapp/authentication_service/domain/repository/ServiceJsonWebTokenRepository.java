package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.ServiceJsonWebToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ServiceJsonWebTokenRepository extends JpaRepository<ServiceJsonWebToken, String> {
    /**
     * delete all expired JWT where
     * @param now this parameter as reference time that design the current instant to know the expired JWT
     */
    public void deleteAllByExpirationDateIsBefore(Instant now);
    public List<ServiceJsonWebToken> findAllByIdService(String idService);

}
