package com.example.backend.dto;

import com.example.backend.entity.CategoryType;
import com.example.backend.entity.Transaction;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TransactionResponseDto {

    private Long id;
    private CategoryType type;
    private Long amount;
    private Long categoryId;
    private String categoryName;
    private String description;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;

    public TransactionResponseDto(Transaction transaction, String categoryName) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.categoryId = transaction.getCategoryId();
        this.categoryName = categoryName;
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.createdAt = transaction.getCreatedAt();
    }
}