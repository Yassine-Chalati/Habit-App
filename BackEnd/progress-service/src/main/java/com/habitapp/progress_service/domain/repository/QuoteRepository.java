package com.habitapp.progress_service.domain.repository;

import com.habitapp.progress_service.domain.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

}