package com.habitapp.progress_service.domain.repository;

import com.habitapp.progress_service.domain.entity.ColumnChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnChartRepository extends JpaRepository<ColumnChart, Long> {

}
