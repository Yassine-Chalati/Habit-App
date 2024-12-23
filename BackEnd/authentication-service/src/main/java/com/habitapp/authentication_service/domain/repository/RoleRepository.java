package com.habitapp.authentication_service.domain.repository;

import com.habitapp.authentication_service.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public boolean existsByRole(String role);
    public Role findByRole(String role);
}
