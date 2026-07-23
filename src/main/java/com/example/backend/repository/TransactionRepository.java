package com.example.backend.repository;

import com.example.backend.entity.CategoryType;
import com.example.backend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 특정 유저의 전체 거래 내역 조회 (최신순)
    List<Transaction> findByUserIdOrderByTransactionDateDescCreatedAtDesc(Long userId);

    // 특정 유저의 기간별 거래 내역 조회
    List<Transaction> findByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    // 특정 유저의 기간별 거래 내역 페이징 조회
    Page<Transaction> findByUserIdAndTransactionDateBetween(
            Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // 1. 특정 기간 & 타입(INCOME/EXPENSE)의 총금액 합계 (결과가 없을 경우 NULL 대신 0 반환)
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.userId = :userId " +
            "AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    long sumAmountByUserIdAndTypeAndDateBetween(
            @Param("userId") Long userId,
            @Param("type") CategoryType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 2. 특정 기간 카테고리별 지출 합계 (지출 타입만 그룹화)
    @Query("SELECT t.categoryId, SUM(t.amount) FROM Transaction t " +
            "WHERE t.userId = :userId " +
            "AND t.type = 'EXPENSE' " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.categoryId " +
            "ORDER BY SUM(t.amount) DESC")
    List<Object[]> sumExpenseByCategoryIdGrouped(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}