package com.habitapp.progress_service.controller;


import com.habitapp.common.http.request_response.chart.ChartInforamtionRequestResponseHttp;
import com.habitapp.progress_service.domain.entity.AreaChart;
import com.habitapp.progress_service.domain.entity.ColumnChart;
import com.habitapp.progress_service.domain.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/chart")
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;

    @PostMapping("/update")
    public ResponseEntity<?> saveOrUpdateCharts(@RequestBody ChartInforamtionRequestResponseHttp chartInforamtionRequestResponseHttp) {
        try {
            chartService.saveOrUpdateCharts(chartInforamtionRequestResponseHttp.getIdUser(), LocalDate.now(), chartInforamtionRequestResponseHttp.isChecked(), chartInforamtionRequestResponseHttp.isUnchecked(), chartInforamtionRequestResponseHttp.isNewHabit(), chartInforamtionRequestResponseHttp.isDeleteHabit());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/area/read")
    public ResponseEntity<?> getAreaChart(@RequestBody Long idUser) {
        try {
            List<AreaChart> areaCharts = chartService.readAreaChart(idUser, LocalDate.now().minusWeeks(7), LocalDate.now());
            return new ResponseEntity<List<AreaChart>>(areaCharts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/column/read")
    public ResponseEntity<?> getColumnChart(@RequestBody Long idUser) {
        try {
            List<ColumnChart> columnCharts = chartService.readColumnChart(idUser, LocalDate.now().minusWeeks(7), LocalDate.now());
            return new ResponseEntity<List<ColumnChart>>(columnCharts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
