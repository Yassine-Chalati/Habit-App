package com.habitapp.notification_service.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.habitapp.notification_service.domain.entity.EntityClassName;

@Repository
public interface RepositoryNameRepository extends JpaRepository<EntityClassName, Long> {
}