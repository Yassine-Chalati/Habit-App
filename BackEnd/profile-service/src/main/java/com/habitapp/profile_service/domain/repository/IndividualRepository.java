package com.habitapp.profile_service.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.habitapp.profile_service.domain.entity.Individual;

@Repository
public interface IndividualRepository extends JpaRepository<Individual, Long> {
}
