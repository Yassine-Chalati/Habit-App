package com.habitapp.progress_service.domain.service;

import com.habitapp.progress_service.domain.entity.AreaChart;
import com.habitapp.progress_service.domain.entity.ColumnChart;

import java.time.LocalDate;
import java.util.List;

public interface ChartService {
    public void saveOrUpdateCharts(Long idUser, LocalDate date, boolean checked, boolean unchecked, boolean newHabit, boolean deleteHabit);
    public List<AreaChart> readAreaChart(Long idUser, LocalDate startDate, LocalDate endDate) throws Exception;
    public List<ColumnChart> readColumnChart(Long idUser, LocalDate startDate, LocalDate endDate) throws Exception;
}
