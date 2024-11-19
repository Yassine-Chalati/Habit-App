package com.habitapp.profile_service.domain.repository;

import com.habitapp.profile_service.domain.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    public Person findByIdAccount(long idAccount);
}
