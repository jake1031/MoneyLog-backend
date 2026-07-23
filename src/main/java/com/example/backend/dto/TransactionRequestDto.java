package com.example.backend.dto;

import com.example.backend.entity.CategoryType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TransactionRequestDto {

    @NotNull(message = "거래 타입은 필수입니다.")
    private CategoryType type;

    @NotNull(message = "금액은 필수입니다.")
    @Min(value = 1, message = "금액은 1원 이상이어야 합니다.")
    private Long amount;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    private Long categoryId;

    @Size(max = 255, message = "내용은 최대 255자까지 입력 가능합니다.")
    private String description;

    @NotNull(message = "거래 일자는 필수입니다.")
    private LocalDate transactionDate;

    public TransactionRequestDto() {}
}