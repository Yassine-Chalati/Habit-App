package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public boolean existsByRole(String role);
    public Role findByRole(String role);
}
