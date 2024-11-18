package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.AccountAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountAuthenticationRepository extends JpaRepository<AccountAuthentication, Long> {
    public List<AccountAuthentication> findAllByUserType(String userType);
}
