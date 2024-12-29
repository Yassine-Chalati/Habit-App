package com.habitapp.habit_service.domain.repository;

import com.habitapp.habit_service.domain.entity.Remainder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RemainderRepository extends JpaRepository<Remainder, Date> {
}
