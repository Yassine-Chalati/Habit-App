package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.Permission;
import com.menara.authentication.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    public boolean existsByPermission(String permission);
    public Permission findByPermission(String permission);
}
