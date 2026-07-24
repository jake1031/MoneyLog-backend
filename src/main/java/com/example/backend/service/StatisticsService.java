package com.example.backend.service;

import com.example.backend.dto.MonthlyStatisticsResponseDto;
import com.example.backend.dto.MonthlyStatisticsResponseDto.CategoryStatDto;
import com.example.backend.entity.Category;
import com.example.backend.entity.CategoryType;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public StatisticsService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public MonthlyStatisticsResponseDto getMonthlyStatistics(Long userId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 1. 총 수입, 총 지출 계산
        long totalIncome = transactionRepository.sumAmountByUserIdAndTypeAndDateBetween(
                userId, CategoryType.INCOME, startDate, endDate);
        long totalExpense = transactionRepository.sumAmountByUserIdAndTypeAndDateBetween(
                userId, CategoryType.EXPENSE, startDate, endDate);

        // 2. 카테고리별 지출 합계 데이터 조회 및 비율 계산
        List<Object[]> categoryGroupedList = transactionRepository.sumExpenseByCategoryIdGrouped(
                userId, startDate, endDate);

        // 카테고리 이름 조회를 위해 카테고리 전체 Map 변환
        Map<Long, String> categoryMap = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<CategoryStatDto> categoryStats = new ArrayList<>();
        for (Object[] row : categoryGroupedList) {
            Long categoryId = (Long) row[0];
            long categoryAmount = (Long) row[1];

            // 비율 계산 (총 지출이 0인 경우 방지)
            double percentage = (totalExpense > 0) ? ((double) categoryAmount / totalExpense) * 100 : 0.0;
            String categoryName = categoryMap.getOrDefault(categoryId, "미지정");

            categoryStats.add(new CategoryStatDto(categoryId, categoryName, categoryAmount, percentage));
        }

        return new MonthlyStatisticsResponseDto(totalIncome, totalExpense, categoryStats);
    }
}