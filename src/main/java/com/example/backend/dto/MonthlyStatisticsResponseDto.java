package com.example.backend.dto;

import java.util.List;

public class MonthlyStatisticsResponseDto {

    private long totalIncome;      // 총 수입
    private long totalExpense;     // 총 지출
    private long netIncome;        // 순수입 (수입 - 지출)
    private List<CategoryStatDto> categoryStats; // 카테고리별 지출 비중

    public MonthlyStatisticsResponseDto(long totalIncome, long totalExpense, List<CategoryStatDto> categoryStats) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netIncome = totalIncome - totalExpense;
        this.categoryStats = categoryStats;
    }

    public long getTotalIncome() { return totalIncome; }
    public long getTotalExpense() { return totalExpense; }
    public long getNetIncome() { return netIncome; }
    public List<CategoryStatDto> getCategoryStats() { return categoryStats; }

    // 카테고리별 합계 및 비중 DTO
    public static class CategoryStatDto {
        private Long categoryId;
        private String categoryName;
        private long amount;
        private double percentage; // 지출 중 해당 카테고리가 차지하는 비율 (%)

        public CategoryStatDto(Long categoryId, String categoryName, long amount, double percentage) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.amount = amount;
            this.percentage = Math.round(percentage * 10.0) / 10.0; // 소수점 첫째자리까지 반올림
        }

        public Long getCategoryId() { return categoryId; }
        public String getCategoryName() { return categoryName; }
        public long getAmount() { return amount; }
        public double getPercentage() { return percentage; }
    }
}