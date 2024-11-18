package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.AccountJsonWebToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AccountJsonWebTokenRepository extends JpaRepository<AccountJsonWebToken, String> {
//    public void deleteByJti(String jti);

//    public List<AccountJsonWebToken> findAllByIdAccountAndExpirationDateAfter(long idAccount, Instant now);

    /**
     * delete all expired JWT where
     * @param now this parameter as reference time that design the current instant to know the expired JWT
     */
    public void deleteAllByExpirationDateIsBefore(Instant now);
    public List<AccountJsonWebToken> findAllByIdAccount(long idAccount);
    public List<AccountJsonWebToken> findAllByUserType(String userType);

}
