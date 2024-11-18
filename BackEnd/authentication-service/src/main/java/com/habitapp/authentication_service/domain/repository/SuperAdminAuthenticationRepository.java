package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.SuperAdminAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminAuthenticationRepository extends JpaRepository<SuperAdminAuthentication, String> {
}
