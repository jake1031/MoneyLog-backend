package com.example.backend.dto;

import com.example.backend.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CategoryRequestDto {

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;

    @NotNull(message = "카테고리 타입은 필수입니다.")
    private CategoryType type;

    // 기본 생성자 (Jackson 파싱용)
    public CategoryRequestDto() {}

    public CategoryRequestDto(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }
}