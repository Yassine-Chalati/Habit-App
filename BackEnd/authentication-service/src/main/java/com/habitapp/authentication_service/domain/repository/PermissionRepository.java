package com.habitapp.authentication_service.domain.repository;


import com.habitapp.authentication_service.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    public boolean existsByPermission(String permission);
    public Permission findByPermission(String permission);
}
