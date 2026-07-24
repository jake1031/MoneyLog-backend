package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class) // created_at, updated_at 자동 관리를 위해 필요
public class Transaction {

    // 거래 식별자 (PK, AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기록한 사용자 (FK -> users.id, NOT NULL)
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    // 분류 카테고리 (FK -> categories.id, NOT NULL)
    @NotNull
    @JoinColumn(name = "category_id", nullable = false)
    private Long categoryId;

    // 수입/지출 구분 (ENUM('INCOME', 'EXPENSE'), NOT NULL)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;

    // 금액 (원 단위, long, NOT NULL, > 0)
    @NotNull
    @Min(value = 1, message = "금액은 0원보다 커야 합니다.") // 1원 이상만 허용
    @Column(nullable = false)
    private Long amount;

    // 메모/설명 (VARCHAR(255), NULL 허용)
    @Column(length = 255) // nullable = true가 기본값
    private String description;

    // 거래 발생일 (DATE, NOT NULL, LocalDate 매핑)
    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    // 등록 시각 (DATETIME, NOT NULL)
    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정 시각 (DATETIME, NOT NULL)
    @NotNull
    @LastModifiedDate // 엔티티 수정(update) 시 현재 시간이 자동으로 갱신됨
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // === 기본 생성자 (JPA 필수) ===
    protected Transaction() {}

    // === 생성자 예시 ===
    public Transaction(Long userId, Long categoryId, CategoryType type, Long amount, String description, LocalDate transactionDate) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public void update(Long categoryId, CategoryType type, Long amount, String description, LocalDate transactionDate) {
        this.categoryId = categoryId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    // === Getter ===
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getCategoryId() { return categoryId; }
    public CategoryType getType() { return type; }
    public Long getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}