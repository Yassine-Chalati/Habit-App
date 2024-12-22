package com.habitapp.authentication_service.domain.repository;

import com.habitapp.authentication_service.domain.entity.DefaultAccountIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultAccountIndividualRepository extends JpaRepository<DefaultAccountIndividual, Long> {
    public DefaultAccountIndividual findDefaultAccountIndividualByEmail(String email);
}
