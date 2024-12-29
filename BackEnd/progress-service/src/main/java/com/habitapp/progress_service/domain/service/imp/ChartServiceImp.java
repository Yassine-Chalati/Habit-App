package com.habitapp.progress_service.domain.service.imp;

import com.habitapp.progress_service.domain.entity.AreaChart;
import com.habitapp.progress_service.domain.entity.ColumnChart;
import com.habitapp.progress_service.domain.repository.AreaChartRepository;
import com.habitapp.progress_service.domain.repository.ColumnChartRepository;
import com.habitapp.progress_service.domain.service.ChartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ChartServiceImp implements ChartService {
    private final ColumnChartRepository columnChartRepository;
    private final AreaChartRepository areaChartRepository;

    @Override
    public void saveOrUpdateCharts(Long idUser, LocalDate date, boolean checked, boolean unchecked, boolean newHabit, boolean deleteHabit) {
        columnChartRepository.findByIdUserAndDate(idUser, date)
                .ifPresentOrElse(
                        columnChart -> {
                            if (checked) {
                                columnChart.setNumberCheckedHabit(columnChart.getNumberCheckedHabit() + 1);
                            }
                            if (unchecked) {
                                columnChart.setNumberCheckedHabit(columnChart.getNumberCheckedHabit() - 1);
                            }
                            columnChartRepository.save(columnChart);
                        },
                        () -> {
                            ColumnChart columnChart = new ColumnChart();
                            columnChart.setIdUser(idUser);
                            columnChart.setDate(date);
                            columnChart.setNumberCheckedHabit(0);
                            if (checked) {
                                columnChart.setNumberCheckedHabit(columnChart.getNumberCheckedHabit() + 1);
                            }
                            if (unchecked) {
                                columnChart.setNumberCheckedHabit(columnChart.getNumberCheckedHabit() - 1);
                            }
                            columnChartRepository.save(columnChart);
                        });

        areaChartRepository.findByIdUserAndDate(idUser, date)
                .ifPresentOrElse(
                        areaChart -> {
                            if (checked) {
                                areaChart.setNumberCheckedHabit(areaChart.getNumberCheckedHabit() + 1);
                            }
                            if (unchecked) {
                                areaChart.setNumberCheckedHabit(areaChart.getNumberCheckedHabit() - 1);
                            }
                            if(newHabit){
                                areaChart.setNumberTotalHabit(areaChart.getNumberTotalHabit() + 1);
                            }
                            if(deleteHabit){
                                areaChart.setNumberTotalHabit(areaChart.getNumberTotalHabit() - 1);
                            }
                            areaChartRepository.save(areaChart);
                        },
                        () -> {
                            AreaChart areaChart = new AreaChart();
                            areaChart.setIdUser(idUser);
                            areaChart.setDate(date);
                            areaChart.setNumberCheckedHabit(0);
                            areaChart.setNumberTotalHabit(0);

                            if(newHabit){
                                areaChart.setNumberTotalHabit(areaChart.getNumberTotalHabit() + 1);
                            }
                            areaChartRepository.save(areaChart);
                        });
    }

    @Override
    public List<AreaChart> readAreaChart(Long idUser, LocalDate startDate, LocalDate endDate) throws Exception {
        return areaChartRepository.findByIdUserAndDateBetween(idUser, startDate, endDate)
                .orElseThrow(
                        () -> new Exception("Area chart not found")
                );
    }

    @Override
    public List<ColumnChart> readColumnChart(Long idUser, LocalDate startDate, LocalDate endDate) throws Exception {
        return columnChartRepository.findByIdUserAndDateBetween(idUser, startDate, endDate)
                .orElseThrow(
                        () -> new Exception("Area chart not found")
                );
    }
}
