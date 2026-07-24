package com.example.backend.controller;


import com.example.backend.dto.MonthlyStatisticsResponseDto;
import com.example.backend.global.common.response.ApiResponse;
import com.example.backend.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlyStatisticsResponseDto>> getMonthlyStatistics(
            @AuthenticationPrincipal Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {

        MonthlyStatisticsResponseDto response = statisticsService.getMonthlyStatistics(userId, yearMonth);
        return ResponseEntity.ok(ApiResponse.success("월별 통계 조회 성공", response));
    }
}