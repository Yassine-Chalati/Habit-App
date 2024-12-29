package com.habitapp.progress_service.domain.repository;

import com.habitapp.progress_service.domain.entity.AreaChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AreaChartRepository extends JpaRepository<AreaChart, Long> {
    Optional<AreaChart> findByIdUserAndDate(Long idUser, LocalDate date);
    Optional<List<AreaChart>> findByIdUserAndDateBetween(Long idUser, LocalDate startDate, LocalDate endDate);
}