package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class) // created_at 자동 입력을 위해 필요
public class Category {

    // 카테고리 식별자 (PK, AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소유 사용자 (FK -> users.id, NOT NULL)
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 카테고리 이름 (식비, 급여 등, VARCHAR(50), NOT NULL)
    @NotNull
    @Column(length = 50, nullable = false)
    private String name;

    // 수입/지출 구분 (ENUM('INCOME', 'EXPENSE'), NOT NULL)
    @NotNull
    @Enumerated(EnumType.STRING) // DB에는 숫자(0, 1)가 아닌 문자열로 저장
    @Column(nullable = false)
    private CategoryType type;

    // 생성 시각 (DATETIME, NOT NULL)
    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // === 기본 생성자 (JPA 필수) ===
    protected Category() {}

    // === 생성자 예시 ===
    public Category(Long userId, String name, CategoryType type) {
        this.userId = userId;
        this.name = name;
        this.type = type;
    }

    // === Getter ===
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public CategoryType getType() { return type; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void update(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }
}
