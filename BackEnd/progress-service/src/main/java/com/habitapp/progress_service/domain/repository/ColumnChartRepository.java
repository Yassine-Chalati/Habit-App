package com.habitapp.progress_service.domain.repository;

import com.habitapp.progress_service.domain.entity.AreaChart;
import com.habitapp.progress_service.domain.entity.ColumnChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ColumnChartRepository extends JpaRepository<ColumnChart, Long> {
    Optional<ColumnChart> findByIdUserAndDate(Long idUser, LocalDate date);
    Optional<List<ColumnChart>> findByIdUserAndDateBetween(Long idUser, LocalDate startDate, LocalDate endDate);

}
